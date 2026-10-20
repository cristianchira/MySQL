"""Open the MySQL books for one topic.

Prints UTF-8 text from PDFs in the repo root, from books/ when that folder
exists, and from the target topic folder. Does not invent chapter text.
Does not delete files.
"""

from __future__ import annotations

import argparse
import sys
from pathlib import Path

import pymupdf


STOP = {
    "and",
    "the",
    "with",
    "for",
    "from",
    "data",
    "type",
    "types",
    "mysql",
    "sql",
    "statement",
    "statements",
}


ALIASES = {
    "innerjoin": ("inner", "join"),
    "leftjoin": ("left", "join"),
    "crossjoin": ("cross", "join"),
    "selfjoin": ("self", "join"),
    "unionvsunionall": ("union",),
    "whereorhaving": ("where", "having"),
    "isnotnullisnull": ("null",),
    "innotin": ("in",),
    "likenotlike": ("like", "wildcard"),
    "betweenand": ("between",),
    "groupby": ("group",),
    "orderby": ("order",),
    "charandvarchar": ("char", "varchar"),
    "binaryandvarbinary": ("binary",),
    "blobandtext": ("blob", "text"),
    "primarykey": ("primary", "key"),
    "foreignkey": ("foreign", "key"),
    "uniquekey": ("unique",),
    "droptruncatedelete": ("drop", "truncate", "delete"),
    "storedprocedures": ("procedure",),
    "userdefinedfunction": ("function",),
    "aggregatefunction": ("aggregate", "count", "sum"),
    "existsnotexists": ("exists",),
    "datedatetimetimestamp": ("date", "datetime", "timestamp"),
    "autoincrement": ("auto", "increment"),
}


def is_mysql_repo(path: Path) -> bool:
    pom = path / "pom.xml"
    if not pom.is_file() or not (path / "src" / "main" / "java").is_dir():
        return False
    text = pom.read_text(encoding="utf-8", errors="replace")
    return "<artifactId>MySQL</artifactId>" in text


def repo_root() -> Path:
    here = Path(__file__).resolve()
    for parent in [here, *here.parents]:
        if is_mysql_repo(parent):
            return parent
        sibling = parent / "MySQL"
        if is_mysql_repo(sibling):
            return sibling
    raise SystemExit("mysql repo root not found above the script")


def norm(value: str) -> str:
    return "".join(ch for ch in value.lower() if ch.isalnum())


def emit(line: str = "") -> None:
    sys.stdout.write(line + "\n")


def page_text(page: pymupdf.Page, limit: int = 4000) -> str:
    text = page.get_text("text").strip()
    if len(text) <= limit:
        return text
    return text[:limit] + "\n…[truncat]"


def topic_keys(topic: str) -> set[str]:
    keys = {norm(topic)}
    parts = []
    for raw in topic.replace("\\", "/").split("/"):
        raw = raw.strip()
        if not raw:
            continue
        piece = norm(raw)
        if piece:
            keys.add(piece)
        for token in raw.replace("-", "_").split("_"):
            folded = norm(token)
            if not folded or folded.isdigit() or folded in STOP:
                continue
            parts.append(folded)
            keys.add(folded)
    last = norm(topic.replace("\\", "/").split("/")[-1]).lstrip("0123456789")
    keys.update(ALIASES.get(last, ()))
    keys.update(ALIASES.get("".join(parts), ()))
    return {key for key in keys if key}


def open_toc(path: Path, keys: set[str]) -> None:
    if path.stat().st_size == 0:
        emit(f"FILE {path.name} ZERO_BYTES NERULAT")
        return
    doc = pymupdf.open(path)
    hits = []
    for level, title, page in doc.get_toc():
        blob = norm(title)
        if any(key in blob or (len(blob) > 4 and blob in key) for key in keys):
            hits.append((level, title, page))
    emit(f"FILE {path.name} pages={doc.page_count} toc_hits={len(hits)}")
    if not hits:
        emit("NO_TOC_HIT")
        doc.close()
        return
    seen: set[int] = set()
    shown = 0
    for _level, title, page in hits:
        emit(f"TOC {title} page={page}")
        if page < 1 or page in seen or page > doc.page_count:
            continue
        seen.add(page)
        if shown >= 6:
            continue
        shown += 1
        text = page_text(doc[page - 1])
        if not text:
            emit(f"--- {path.name} p{page} EMPTY_TEXT ---")
            continue
        emit(f"--- {path.name} p{page} ---")
        emit(text)
    if len(seen) > shown:
        emit(f"TOC_TEXT_CAPPED shown={shown} pages={len(seen)}")
    doc.close()


def open_sample(path: Path, pages: int) -> None:
    if path.stat().st_size == 0:
        emit(f"FILE {path.name} ZERO_BYTES NERULAT")
        return
    doc = pymupdf.open(path)
    emit(f"FILE {path.name} pages={doc.page_count}")
    empty = 0
    count = min(pages, doc.page_count)
    for index in range(count):
        text = page_text(doc[index])
        if not text:
            empty += 1
            emit(f"--- {path.name} p{index + 1} EMPTY_TEXT ---")
            continue
        emit(f"--- {path.name} p{index + 1} ---")
        emit(text)
    if count and empty == count:
        emit("NO_TEXT_LAYER")
    doc.close()


def teaching_folder(root: Path, topic: str) -> Path | None:
    base = root / "src" / "main" / "java"
    direct = base / topic
    if direct.is_dir():
        return direct
    keys = topic_keys(topic)
    skip = {"fast", "example"}
    exact = []
    for child in base.rglob("*"):
        if not child.is_dir() or norm(child.name) in skip:
            continue
        blob = norm(child.name)
        if blob in keys:
            exact.append(child)
    if len(exact) == 1:
        return exact[0]
    if len(exact) > 1:
        emit("AMBIGUOUS")
        for child in exact:
            emit(f"  {child.relative_to(base)}")
        return None
    return None


def name_matches(path: Path, keys: set[str]) -> bool:
    blob = norm(path.stem)
    return any(key in blob or blob in key for key in keys if len(key) > 3)


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--topic", required=True)
    args = parser.parse_args()
    root = repo_root()
    keys = topic_keys(args.topic)
    emit(f"ROOT {root}")
    emit("KEYS " + " ".join(sorted(keys)))

    emit("CANONICAL")
    root_pdfs = sorted(path for path in root.glob("*.pdf"))
    if not root_pdfs:
        emit("NO_CANONICAL_PDF")
    for path in root_pdfs:
        emit(f"  {path.name} bytes={path.stat().st_size}")
        open_toc(path, keys)

    emit("BOOKS")
    books = root / "books"
    if not books.is_dir():
        emit("NO_BOOKS_DIR")
    else:
        matched = [path for path in sorted(books.rglob("*.pdf")) if name_matches(path, keys)]
        if not matched:
            emit("NO_BOOKS_HIT")
        for path in matched:
            emit(f"BOOK {path.relative_to(root)}")
            open_sample(path, 2)

    emit("LOCAL")
    folder = teaching_folder(root, args.topic)
    if folder is None:
        emit(f"NO_TEACHING_FOLDER {args.topic}")
        return
    emit(f"TEACHING {folder.relative_to(root / 'src' / 'main' / 'java')}")
    local = sorted(folder.rglob("*.pdf"))
    if not local:
        emit("NO_LOCAL_PDF")
    for path in local:
        emit(f"LOCAL {path.relative_to(root / 'src' / 'main' / 'java')}")
        open_sample(path, 2)


if __name__ == "__main__":
    sys.stdout.reconfigure(encoding="utf-8")
    main()
