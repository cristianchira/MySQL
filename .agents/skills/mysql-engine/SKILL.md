---
name: mysql-engine
description: >-
  Nu prelua comanda de aici. "update mysql folder name", "update mysql <folder>", "upgrade mysql <folder>" și "update mysql Modul" se aplică integral din folderul learn-mysql-fast: SKILL.md până la capăt, catalog.md și scripts/open_books.py. Fiecare secțiune care ține de comandă se execută. Nu se rezumă și nu se sare un pas.
  Același folder learn-mysql-fast stă în repo-ul MySQL și în AdvancedJava/.agents/skills/learn-mysql-fast. Catalogul este un singur fișier, learn-mysql-fast/catalog.md. Nu se ține o a doua copie. Acest fișier rămâne.
---

# MySQL Engine

Comanda `update mysql folder name` se aplică din folderul `learn-mysql-fast` (`SKILL.md`, `catalog.md`, `scripts/open_books.py`). Acest fișier rămâne. Nu ține un al doilea catalog și nu ține o copie a scriptului.

Comanda este `update mysql <nume>`. `update mysql folder name` este aceeași comandă: `<nume>` este folderul sau aliasul din catalog (`where`, `_2_WHERE`, `inner`). Deschizi repo-ul MySQL, citești `learn-mysql-fast/SKILL.md` până la capăt și lucrezi doar în `src/main/java`. Nu aplici staff-architect-engine. Nu aplici design-pattern-engine. Nu aplici learn-hibernate-fast.

Motor de poartă pentru repo-ul MySQL. Ținta este expertul de interviu: recunoaște mecanismul din propoziția SQL, din `EXPLAIN` sau din eroare, spune efectul asupra rândurilor, schemei, indexului sau tranzacției, îl deosebește de vecin, arată eșecul și alege sub o constrângere nouă.

`update` fără `mysql` rămâne staff-architect-engine. `update hb` rămâne learn-hibernate-fast. `update dp` rămâne design-pattern-engine. „explică-mi rapid”, „pe scurt” și „learn fast” pe un mecanism MySQL sunt learn-mysql-fast. Acest motor nu preia fraza.

La `update mysql <folder>`, `upgrade mysql <folder>`, `update mysql Modul` și `update mysql <topic>` rezolvat la un folder, citești `.agents/skills/learn-mysql-fast/SKILL.md` până la capăt și îl aplici integral pe folderul din comandă: fiecare secțiune MY.0–MY.10 care ține de comandă, apoi Lecție rapidă. Nu un rezumat. Nu un pas sărit. Poarta de aici rămâne. Fast nu o înlocuiește.

## MY.0 Nu se șterge. Se adaugă. Se corectează doar faptul greșit.

Regula bate orice obicei care golește, înlocuiește sau șterge.

- Nu se șterge folder, fișier, clasă, metodă, câmp, exemplu, PDF, README sau comentariu corect. Nu se golește un fișier ca să fie rescris. Nu se redenumește un topic ca să dispară originalul.
- Nu se importă ștergerea de `*Brief.java` sau de folder `*Terms`. Dacă există, rămân. Pagina scurtă pe care o adaugi este `fast`.
- Clasele deja scrise, inclusiv cele în care SQL-ul stă într-un comentariu, sunt `ACOPERIT ÎN SRC` când predau mecanismul. Le citești. Nu le copiezi și nu le înlocuiești cu JDBC.
- Un fapt greșit se corectează. Propoziția veche rămâne pe rândul următor, marcată `HISTORICAL`, cu regula citită în tur lângă ea.
- Întrebările scrise de utilizator sub `Întrebări adăugate`, și orice întrebare deja prezentă peste cele 50, rămân.

```text
ARTEFACT VERIFIED ≠ STUDENT MASTERED ≠ EXPERT
```

Harta de foldere și perechile de interviu stau în [catalog.md](../learn-mysql-fast/catalog.md). Citești catalogul la fiecare `update mysql`, înainte de primul `.java`.

## MY.1 Activare

| Spune utilizatorul | Mod |
|---|---|
| `update mysql <folder>`, `update mysql folder name`, `upgrade mysql <folder>`, `update mysql Modul`, `update mysql <topic>` | Artefact, integral după learn-mysql-fast. Rezolvi numele din catalog. Tabel de acoperire, apoi tabel de cărți, apoi fișierul de topic și folderul `fast`. Un pas sărit nu închide comanda. |
| `învață`, `learn`, `recall`, `teach-back`, `mastery`, `exercițiu`, `spaced review` | Study → Hide → Recall → Predict. Cheia rămâne ascunsă până la încercare. Doar când subiectul este în catalog. |
| `explică-mi rapid`, `pe scurt`, `learn fast` | learn-mysql-fast. Acest motor nu preia fraza. |
| `debug` | Simptomul și ipoteza studentului preced cauza. Propoziția, planul și lacătul preced bănuiala. |
| `design from scratch` | Forțe, efectul asupra rândurilor sau schemei, mecanismul refuzat. Fără SQL-ul desenat înainte de încercare. |
| „sunt întrebările suficiente?” | Filtrul MY.6 din learn-mysql-fast. Rescrii ce pică doar dacă afirmația e greșită; o întrebare corectă în plus rămâne. |

Subiectul unei propoziții din artefact este mecanismul. Click-ul din Workbench nu este mecanismul.

## Ce se execută din learn-mysql-fast

După acest fișier, citești `learn-mysql-fast/SKILL.md` și execuți, în ordine, secțiunile care țin de comandă:

1. MY.2 rezolvă numele din catalog. `server` și `query` nu sunt foldere: inventar, apoi fiecare copil care nu este `VERIFIED`. Linia așteaptă până trec toți.
2. MY.3 deschide PDF-urile cu `python .agents/skills/learn-mysql-fast/scripts/open_books.py --topic "<folder>"` din rădăcina repo-ului MySQL și aduce pagina din manualul `mysql.version`.
3. MY.4 scrie tabelul de acoperire, apoi aduce în `pom.xml` ultimul Java, ultima serie LTS MySQL și Connector/J citite în tur. Numerele nu se inventează. Nu se scrie o parolă.
4. MY.5 scrie cardul, best practices și trece blocantele. O metodă care doar povestește propoziția nu este demonstrația.
5. MY.6 scrie cele 5 întrebări și, la creare, exact 50 în `<Stem>_Staff_Interview`.
6. MY.7 pune cele trei trepte pe fiecare linie de cod nouă.
7. MY.8 scrie un singur folder `fast`: `<Stem>_Fast`, `<Stem>_Questions` gol, `<Stem>_Staff_Interview`, `<Stem>_Terms`.
8. Lecție rapidă completează cele patru clase. Nu șterge ce există.
9. MY.9 se aplică doar la învățare, nu la `update mysql`.
10. MY.10 este lista care oprește închiderea. Raportul vine doar după ce fiecare fișier din comandă a trecut poarta, inclusiv copiii.

Dacă un punct din learn-mysql-fast nu ține, nu afișezi `TASK TERMINAT 100%`. Scrii `TASK TERMINAT 100%` doar după ce fiecare fișier din comandă a trecut poarta. Toate se actualizează, fără excepție. Un fișier lăsat neatins, `PĂSTRAT`, `PARTIAL` sau `LIPSEȘTE` oprește linia.
