---
name: learn-mysql-fast
description: >-
  Skill-ul de MySQL. Se aplică la "update mysql folder name", "update mysql <folder>", "update mysql <topic>", "upgrade mysql <folder>" și "update mysql Modul".
  Numele de după mysql este folderul din repo-ul MySQL (src/main/java), rezolvat din catalog.md: "update mysql where" este _2_WHERE, "update mysql inner" este _1_INNER_JOIN.
  „explică-mi rapid”, „pe scurt” și „learn fast” pe un mecanism MySQL sunt secțiunea Lecție rapidă. Nu șterge ce există.
  La update mysql, pom.xml primește ultimele versiuni stabile citite în tur: Java, MySQL LTS, Connector/J. Numerele nu se îngheață aici.
  update mysql se execută integral după acest fișier: fiecare secțiune care ține de comandă, fără rezumat și fără pas sărit.
  La sfârșit se verifică sincronizarea. Doar la 100% se afișează TASK TERMINAT 100%, și doar după ce fiecare fișier din comandă a trecut poarta, fără excepție.
  Nu se activează pe "update" fără "mysql", nici pe "update hb", nici pe "update dp", nici pe "update sb". Nu înlocuiește staff-architect-engine.
---

# learn-mysql-fast

Acest folder este skill-ul de MySQL. `SKILL.md` este poarta. `catalog.md` este harta de foldere. `scripts/open_books.py` deschide PDF-urile.

Comanda este `update mysql <nume>`. `update mysql folder name` este aceeași comandă: `<nume>` este folderul sau aliasul din catalog (`where`, `_2_WHERE`, `inner`). Deschizi repo-ul MySQL, citești acest fișier până la capăt și lucrezi doar în `src/main/java`. Nu aplici staff-architect-engine. Nu aplici design-pattern-engine. Nu aplici learn-hibernate-fast. Nu aplici learn-spring-boot-fast.

Motor de poartă pentru repo-ul MySQL. Ținta este expertul de interviu: recunoaște mecanismul din propoziția SQL, din `EXPLAIN` sau din eroare, spune efectul asupra rândurilor, schemei, indexului sau tranzacției, îl deosebește de vecin, arată eșecul și alege sub o constrângere nouă.

`update` fără `mysql` rămâne staff-architect-engine. `update hb` rămâne learn-hibernate-fast. `update dp` rămâne design-pattern-engine. `update sb` rămâne learn-spring-boot-fast. „explică-mi rapid”, „pe scurt” și „learn fast” pe un mecanism MySQL sunt secțiunea Lecție rapidă de la sfârșitul acestui fișier.

La `update mysql <folder>`, `upgrade mysql <folder>`, `update mysql Modul` și `update mysql <topic>` rezolvat la un folder, aplici și secțiunea Lecție rapidă pe folderul din comandă. Poarta de mai sus rămâne.

`update mysql` se face 100% după acest fișier. Citești `SKILL.md` până la capăt, apoi catalogul, apoi execuți fiecare secțiune care ține de comandă: versiunile, tabelul de acoperire, tabelul de cărți, fișierul de topic (card, best practices, cinci întrebări), folderul `fast` cu cele patru clase și lecția rapidă. Nu sari un pas. Nu înlocuiești un artefact cu un rezumat. Nu declari gata cât un punct din MY.5 sau MY.10 încă stă.

`update mysql` fără nume nu închide un folder. Numele se cere din catalog.

Când task-ul este terminat, îl verifici încă o dată, punct cu punct, contra cerințelor din acest fișier. Dacă sincronizarea este 100%, ultima linie a răspunsului este exact:

```text
TASK TERMINAT 100%
```

Dacă un punct nu ține, nu afișezi linia. Scrii `TASK TERMINAT 100%` doar după ce fiecare fișier din comandă a trecut poarta. Toate se actualizează, fără excepție. Un fișier lăsat neatins, `PĂSTRAT`, `PARTIAL` sau `LIPSEȘTE` oprește linia.

La `update mysql`, înainte de cod, aduci `pom.xml` la ultimele versiuni stabile citite în tur: Java, serverul MySQL și Connector/J. Numerele nu se îngheață aici.

Același folder stă în repo-ul MySQL și în `AdvancedJava/.agents/skills/learn-mysql-fast`.

## MY.0 Nu se șterge. Se adaugă. Se corectează doar faptul greșit.

Regula bate orice obicei din design-pattern-engine sau din staff-architect-engine care golește, înlocuiește sau șterge.

- Nu se șterge folder, fișier, clasă, metodă, câmp, exemplu, PDF, README sau comentariu corect. Nu se golește un fișier ca să fie rescris. Nu se redenumește un topic ca să dispară originalul. Numerotarea și ortografia de pe disc rămân, inclusiv `TAble` și `GRPUP`.
- Nu se importă ștergerea de `*Brief.java`, de folder `*Terms` sau de `problem/` gol. Dacă există, rămân. Pagina scurtă pe care o adaugi este `fast`.
- Clasele deja scrise, inclusiv cele în care SQL-ul stă într-un comentariu, sunt `ACOPERIT ÎN SRC` când predau mecanismul. Le citești. Nu le copiezi într-un al doilea vocabular și nu le înlocuiești cu JDBC ca să pară o demonstrație.
- Un comentariu corect, chiar de o linie, rămâne. Nu îl rescrii ca să bifezi treptele. Treptele din MY.7 sunt obligatorii pe linia nouă pe care o scrii tu.
- Un fapt greșit se corectează. Propoziția veche rămâne pe rândul următor, marcată `HISTORICAL`, cu regula citită în tur lângă ea. Fără marcaj, corecția pică: textul vechi pare regula curentă.
- Întrebările scrise de utilizator sub `Întrebări adăugate`, și orice întrebare deja prezentă peste cele 50, rămân.

```text
ARTEFACT VERIFIED ≠ STUDENT MASTERED ≠ EXPERT
```

Harta de foldere, perechile cu care se confundă mecanismul și deschiderea cărților stau în [catalog.md](catalog.md). Citești catalogul la fiecare `update mysql`, înainte de primul `.java`.

## MY.1 Activare

| Spune utilizatorul | Mod |
|---|---|
| `update mysql <folder>`, `update mysql folder name`, `upgrade mysql <folder>`, `update mysql Modul`, `update mysql <topic>` | Artefact, integral după acest fișier. Rezolvi numele din catalog. Tabel de acoperire, apoi tabel de cărți, apoi fișierul de topic și folderul `fast`. Un pas sărit nu închide comanda. |
| `învață`, `learn`, `recall`, `teach-back`, `mastery`, `exercițiu`, `spaced review` | Study → Hide → Recall → Predict. Cheia rămâne ascunsă până la încercare. Doar când subiectul este în catalog. |
| `explică-mi rapid`, `pe scurt`, `learn fast` | Secțiunea Lecție rapidă din acest fișier. |
| `debug` | Simptomul și ipoteza studentului preced cauza. Propoziția, planul și lacătul preced bănuiala. |
| `design from scratch` | Forțe, efectul asupra rândurilor sau schemei, mecanismul refuzat. Fără SQL-ul desenat înainte de încercare. |
| „sunt întrebările suficiente?” | Filtrul MY.6. Rescrii ce pică doar dacă afirmația e greșită; o întrebare corectă în plus rămâne. |

Subiectul unei propoziții din artefact este mecanismul: efectul pe rânduri, schemă, index sau tranzacție, vecinul, eșecul. Pică propoziția despre fișier, folder, glosar, skill sau metoda de studiu. Pică și propoziția despre Workbench: click-ul nu este mecanismul. Mecanismul este propoziția pe care o execută serverul.

## MY.2 Rezolvare

`update mysql where` și `update mysql WHERE` sunt același folder: `_2_SQL_Statements/_2_DML/_1_SELECT/_2_WHERE`. `update mysql inner` este `_10_JOINs/_1_INNER_JOIN`. Numele se rezolvă din catalog, nu din memorie. Clasa deja scrisă în folder este `ACOPERIT ÎN SRC`: o citești, nu o copiezi. `update mysql` fără cale lovește folderul din catalog. Calea explicită (`update mysql _10_JOINs/_3_LEFT_JOIN`) lovește acea cale.

Un `update mysql` pe un nume de familie care nu este și un folder (`server`, `query`) scrie inventarul, apoi actualizează fiecare copil care nu este `VERIFIED`. Nu scrii `TASK TERMINAT 100%` până când fiecare copil din inventar a trecut poarta. `statements`, `joins`, `constraints`, `types`, `routines`, `triggers`, `views` și `optimization` sunt foldere: le lovești pe ele, nu familia. `fast` al acelui folder predă cum alegi între copiii lui. Nu predă fiecare mecanism în întregime. Copiii trec și ei poarta, fiecare cu fișierul lui. Părintele nu se închide cât un copil este neatins.

## MY.3 Cărțile și manualul

Nu ține o listă de titluri în acest skill. Discul și `pom.xml` din tur sunt sursa. Nu există arbore `Luv2code`. PDF-urile de curs stau în folderul topicului.

Deschizi PDF-urile cu:

```text
python .agents/skills/learn-mysql-fast/scripts/open_books.py --topic "<folder din catalog>"
```

din rădăcina repo-ului MySQL. Scriptul nu inventează text. `get_text` gol nu înseamnă „nu are capitolul”: rândul este `FĂRĂ STRAT DE TEXT`. Un PDF de 0 octeți este `NERULAT AICI`, fără citat inventat. Dacă scriptul cade fiindcă lipsește `pymupdf`, rândurile PDF sunt `NERULAT AICI`. Nu inventa capitolul și nu instala pachetul doar ca să umpli tabelul.

Manualul de referință se deschide în tur la versiunea `mysql.version` scrisă în `pom.xml` după ultima serie LTS stabilă, nu la o versiune mai veche lăsată dinainte și nu la o serie Innovation:

```text
https://dev.mysql.com/doc/refman/<major.minor>/en/
```

Deschizi pagina mecanismului, nu doar cuprinsul. Un rând de manual fără pagina adusă în tur este inventat și pică. Dacă aducerea eșuează, rândul este `NERULAT AICI`.

În fișier, `BOOK THEORY` este sinteza: efectul pe rânduri, schemă, index sau tranzacție, limita și eroarea din paginile deschise. Fără număr de pagină. Fără „vezi capitolul”. Dacă un PDF nu are mecanismul, o propoziție spune asta. Statusul unui mecanism scos (query cache, lățimea de afișare la întregi) este cel din manualul versiunii citite, nu o propoziție înghețată în catalog.

```markdown
## Cărți: <folder>
| Sursă | Capitol sau item | Teorie | Exemplu | Metodă |
|---|---|---|---|---|
```

Un rând pentru manualul versiunii din pom. Un rând pentru fiecare PDF pe care scriptul l-a deschis. Un fișier deschis lăsat în afara tabelului înseamnă că update-ul nu este gata.

## MY.4 Înainte de cod

Nu deschizi un `.java` până nu sunt în conversație ambele tabele.

```markdown
## Acoperire foldere: <folder>
| Unitate | Cale în src | Status |
|---|---|---|
```

Status: `DA` / `ACOPERIT ÎN SRC` / `LIPSEȘTE` / `ÎN PLUS` / „nu se creează”. Nu crea un topic care nu este în catalog. Nu duplica o cale `ACOPERIT ÎN SRC`. `LIPSEȘTE` se acoperă adăugând, nu mutând un demo existent.

La `update mysql`, versiunile nu rămân cele găsite în `pom.xml` dacă sunt în urmă. În tur, înainte de primul `.java`, cauți ultimele versiuni stabile și le scrii în `pom.xml`. Nu le ții minte în acest skill. Nu inventa un număr.

- Java: ultimul release Java SE. Îl scrii în `java.version`, `maven.compiler.source`, `maven.compiler.target` și `maven.compiler.release`.
- MySQL: ultima serie LTS stabilă (GA). O serie Innovation, RC sau un milestone nu este ultima stabilă. O scrii în `mysql.version`, ca `major.minor`.
- Connector/J: ultima versiune stabilă care merge cu Java și cu serverul alese în tur. O scrii ca dependență. Dacă în tur nu există un server care răspunde, dependența tot se scrie; rularea demo-ului rămâne `NERULAT AICI`.

Dacă compilarea pică pe ultimul Java, rămâi pe ultimul Java pe care release-ul chiar compilează și scrii asta la `VERSION`. Numele bazei, utilizatorul și portul se citesc din configurarea repo-ului în tur. Nu le inventa. Nu scrie o parolă nouă în `pom.xml`.

## MY.5 Poarta de expert

Un punct blocant pică fișierul de topic. Folderul este `VERIFIED` când fișierul de topic și cele patru clase din `fast` trec. Clasele deja scrise rămân. Le atingi doar ca să corectezi un fapt greșit, cu `HISTORICAL`. Nu le rescrii ca să bifeze cardul. Nu lași un fișier neatins. Fiecare fișier din folderul comenzii, inclusiv copiii, trece poarta. `PĂSTRAT` înseamnă că fișierul nu a trecut și oprește `TASK TERMINAT 100%`.

### Blocante pe fișierul de topic

1. Lipsește cardul MY.5 pe fișierul de topic, sau eticheta și valoarea stau pe același rând. Cardul se adaugă. Nu înlocuiește comentariul deja corect.
2. `BOOK THEORY` scris fără sursa deschisă în tur, sau care trimite la pagină în loc să spună efectul.
3. Metoda citată la demonstrație nu conține propoziția. Un comentariu care povestește efectul, sau un `String` care descrie SQL-ul fără să fie propoziția, nu este demonstrația. Propoziția nouă stă în cod, gata de trimis la server.
4. Codul nou nu compilează pe release-ul din pom. Un demo care are nevoie de server și serverul nu răspunde în tur este `NERULAT AICI`, nu o rulare inventată.
5. Mecanismul vecin lipsește, sau fraza poate fi lipită neschimbată pe alt topic din catalog.
6. Întrebările pică filtrul MY.6.
7. O linie de cod nouă fără cele trei trepte din MY.7, sau un comentariu nou care repetă sintaxa.
8. O corecție care a șters propoziția greșită în loc să o lase marcată `HISTORICAL`.

### Card

Eticheta se termină cu `:`. Textul stă pe rândul următor, indentat. Dacă fișierul are deja un antet corect, cardul se adaugă după el.

```text
TOPIC:
    mecanismul și efectul pe rânduri, schemă, index sau tranzacție, într-o propoziție
FAMILIE:
    Statements, Schema, Server, Types, Joins, Routines, Security, Transaction sau Optimization
VERSION:
    Java, MySQL și Connector/J din pom, ultimele stabile scrise în tur
INTENT:
    problema recurentă și ce rămâne în tabelă după propoziție
MECHANISM:
    cine execută propoziția, ce se blochează, când se vede rezultatul
LOOKALIKES:
    mecanismul cu care se confundă la interviu și condiția care le separă
BOOK THEORY:
    sinteza din manualul și PDF-urile deschise în tur
BOOK EXAMPLE:
    metoda care conține propoziția
REPO DEMO:
    calea din src/main/java
BREAK LAB:
    cel mai mic SQL greșit și simptomul observabil
TRADE-OFFS:
    cealaltă unealtă reală și când o alegi
ANTI-PATTERNS:
    propoziția periculoasă pe acest mecanism
STAFF QUESTIONS:
    metodele care execută întrebările
NOVEL TRANSFER:
    domeniul nou, care nu este demo-ul din repo
SPACED REVIEW:
    o zi, șapte zile, treizeci de zile
```

### Best practices

Set închis. O situație pe care mecanismul nu o are se scrie `nu are situația`, cu motivul. O practică pe care o poți lipi pe alt topic pică. Numele situației stă pe un rând. Lanțul `->` stă pe rândul următor. Blocul se adaugă. Nu înlocuiește un best-practice deja corect.

```text
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE -> CLASA ȘI METODA -> SQL SAU STARE OBSERVABILĂ -> CE NU GARANTEAZĂ
    2. Eșecul cel mai mic
           -> DECIDE -> CLASA ȘI METODA -> SQL SAU STARE OBSERVABILĂ -> CE NU GARANTEAZĂ
    3. Granița
           -> ce este mecanismul MySQL și ce este doar JDBC, doar Workbench sau doar o convenție de nume
    4. Alternativa
           -> mecanismul vecin și condiția în care schimbi alegerea
    5. Anti-patternul
           -> propoziția periculoasă și efectul ei
    6. Dovada
           -> metoda din repo care arată propoziția, planul sau eroarea
    7. Interviu
           -> efect, un SQL, o consecință, într-un răspuns de două minute
```

Situațiile 3–7 au aceeași rupere de rând.

## MY.6 Filtrul de interviu

În fișierul de topic, minim 5 întrebări, fiecare cu enunț, capcană, cheie, demonstrație. Formele, toate:

```text
1. ce problemă rezolvă mecanismul
2. ce se întâmplă cu rândurile, schema, indexul sau tranzacția
3. de ce nu este mecanismul vecin
4. cum eșuează când este folosit greșit
5. ce alegi în locul lui și în ce condiție
```

Capcana este o propoziție pe care un candidat o spune și care este periculoasă în producție. Cheia numește mecanismul, efectul observabil și decizia.

`<Stem>_Staff_Interview`, la creare, are exact 50 de întrebări, specializate pe acest mecanism:

```text
10 recunoaștere din SQL, EXPLAIN sau eroare
10 rânduri, index, lacăt și momentul în care se vede efectul
10 deosebire de mecanismul vecin
10 eșec în producție
10 trade-off, inclusiv când rămâi în aplicație și nu pe server
```

Dacă fișierul există și are mai puține, adaugi până la distribuție. Dacă are mai multe, le păstrezi. O întrebare care poate fi lipită pe alt topic pică. Banca nu se copiază din catalog. La `learn` și `recall` se dă enunțul; capcana și cheia rămân ascunse până la încercare. În fișier, cheia este scrisă.

## MY.7 Comentarii

Orice linie de cod pe care o adaugi are deasupra comentariul ei, pe trei trepte. Linia de cod este orice linie care nu e goală și nu e deja comentariu: declarație, semnătură, apel, atribuire, `return`, `new`, acoladă, adnotare, propoziție SQL ținută într-un `String` sau într-un text bloc. Comentariul stă deasupra, nu pe aceeași linie. Un bloc nu acoperă mai multe linii de cod.

Nu mături un `.java` neatins ca să îi pui treptele. Treptele se adaugă pe codul nou. Pe o linie atinsă fiindcă afirmația ei este falsă, corectezi afirmația și lași textul vechi marcat `HISTORICAL`.

Cele trei etichete sunt obligatorii pe codul nou, în română, în această ordine. Numele de SQL rămân în engleză. Fiecare etichetă adaugă un fapt pe care cea de deasupra nu l-a spus.

```text
Începător: ce face linia cu rândurile sau cu schema, fără jargon de plan.
Producție: propoziția, așa cum se tastează, și ce se schimbă. Ce se rupe dacă linia dispare.
Expert: mecanismul vecin, forța, ce nu garantează linia. O propoziție care nu poate fi lipită pe alt topic.
```

```java
// Începător: rămân doar angajații care au un departament potrivit. Angajatul fără departament nu apare.
// Producție: INNER JOIN ON compară cheile. Fără pereche, rândul din stânga cade. LEFT JOIN l-ar fi păstrat, cu NULL la dreapta.
// Expert: INNER JOIN nu filtrează după grupare și nu este un index. Fără index pe coloana din ON, planul tot poate citi ambele tabele. Linia nu ține lacăt după ce rezultatul a plecat.
String sql = """
        SELECT e.name, d.name
        FROM employee e
        INNER JOIN department d ON d.id = e.department_id
        """;
```

Un comentariu nou care repetă sintaxa (`// face join`, `// scrie select`) pică la fel ca o treaptă lipsă.

Pe clasa de topic și pe fiecare metodă nouă care predă mecanismul rămân și nivelurile de metodă. Ele nu înlocuiesc comentariul de linie. Nivelurile stau într-un `/* */` imediat deasupra declarației, nu într-un `/** */` după un block tag. Dacă metoda există deja și are un comentariu corect, nu îi pui al doilea bloc.

```text
🟢 Începător
    Ce este            mecanismul, ce forță primește, ce stare lasă în tabelă sau în tranzacție
    De ce e nevoie     ce rând lipsește sau ce eroare apare dacă mecanismul lipsește
🟡 Producție
    Cum se scrie       propoziția, așa cum se tastează
🔴 Staff
    Unde               planul, lacătul, mecanismul vecin
Pași                   corpul, numerotat
```

Definiția de termen, singura formă pe care o adaugi în glosar:

```text
Definiția unei îmbinări interne (inner join):
       Inner join păstrează doar rândurile care au pereche în ambele tabele.

Exemplu:
       Joins_Terms.explainInnerJoin()
       Un departament fără manager nu apare în rezultat.
```

Titlul este `Definiția unei <nume în română> (<termen>):`. Propozițiile stau pe rândul următor, la 7 spații. În comentariu, blocul nu poartă marginea ` * `. `Exemplu:` stă singur. Dedesubt, metoda din clasa de glosar care execută termenul. O definiție deja corectă nu se rescrie.

## MY.8 Artefactele din `fast`

Un singur folder `fast` în folderul din comandă. Nu în fiecare copil. Dacă `fast` există, îl completezi. Nu creezi al doilea.

```text
stem     = numele topicului, fără prefixul _<cifre>_, PascalCase, fără _
           _10_JOINs → Joins
           _2_WHERE → Where
           _15_WHERE_OR_HAVING → WhereOrHaving
           _1_INNER_JOIN → InnerJoin
folder   = fast
lecție   = <Stem>_Fast.java          main doar pentru exemplul minim
faq      = <Stem>_Questions.java     corp gol
interviu = <Stem>_Staff_Interview.java   50 la creare, fără main de recall
glosar   = <Stem>_Terms.java         în același folder
```

Dacă un `*Fast.java` există deja, îl completezi și îi păstrezi numele. Nu creezi al doilea. Nu creezi `*Brief.java`. Dacă un `*Brief.java` există, îl lași.

Glosarul nu ia ultimul segment când mai mulți copii s-ar ciocni (`Join`, `Select`, `Key`). Primul termen este mecanismul. Apoi rolurile din pagina deschisă: tabelă, coloană, index, lacăt sau tranzacție, așa cum le numește manualul. Termenii scriși de utilizator sub `Terms & Definitions` rămân; la update-ul următor li se scrie definiția, de la primul care nu are exemplu.

Cele trei clase lean (`_Fast`, `_Questions`, `_Staff_Interview`) primesc cardul, textul glosarului și etichetele. Nu există pptx. Lipsa pptx-ului nu pică poarta.

Contractul lecției, al FAQ-ului și al bancii este secțiunea Lecție rapidă. Exemplul din `<Stem>_Fast` se compilează în tur, pe release-ul din pom. Se rulează pe server doar dacă un MySQL răspunde în tur. Altfel rămâne `NERULAT AICI`.

## MY.9 Învățare

Se aplică la `învață`, `learn`, `recall`, `teach-back`, `mastery`, `exercițiu`, `spaced review`, `debug`, `design from scratch` când subiectul este în catalog. Nu se aplică la `update mysql`: artefactul conține cheia.

```text
Study → Hide → Recall → Predict → Implement → Break → Compare → Gap → Retry → Transfer → Review
```

Study dă efectul și momentul în care se vede (sfârșitul propoziției, commit, propoziția următoare), apoi se oprește. Predict cere ce rând rămâne, ce eroare apare sau ce ține lacătul, fără să rulezi înainte de predicție. Transfer schimbă domeniul: nu aceeași tabelă din repo, cu alt nume.

Starea stă într-un singur fișier, `UTIL/student_state.md`, în rădăcina repo-ului MySQL. Îl citești la începutul sesiunii de învățare. Îl scrii la sfârșit doar adăugând răspunsul, rularea sau observația din tur. Nu ștergi rânduri vechi. Data vine din ceasul sesiunii. Dacă lipsește, îl creezi gol, fără istoric inventat.

```markdown
## Stare topic
| Topic | Cale | Artefact | Student | Mech | Fail | Impl | Trade | Transfer | Mod | Ultima sesiune | Review +1 | Review +7 | Review +30 |

## Registru concepții greșite
| Data | Topic | MISCONCEPTION | CONFLICT | CORRECT MODEL | COUNTEREXAMPLE | RETRY | TRANSFER | SPACED CHECK |
```

Un indiciu pe încercare nouă. Indiciul nu conține SQL-ul reparat și nu numește mecanismul care închide întrebarea de recunoaștere. După trei încercări eșuate, sau la „arată-mi”: explici mecanismul pe un exemplu înrudit, marchezi exercițiul `GUIDED`, plafonezi la 2 axele atinse. `MASTERED` cere ≥3 pe toate axele, Transfer ≥3, niciun `GUIDED` nesuprapus de un transfer independent, și un review spațiat trecut fără indiciu. `EXPERT` cere 4 pe toate axele. Studentul nu se auto-notează. `VERIFIED` nu este niciuna dintre ele.

## MY.10 Ce nu se declară gata

- un pas din acest fișier sărit, un artefact înlocuit cu un rezumat, sau închidere cât un punct din MY.5 sau MY.10 încă stă
- linia `TASK TERMINAT 100%` afișată fără verificarea de la sfârșit, sau cât un punct din acest fișier nu ține, sau cât un fișier din comandă este neatins, `PĂSTRAT`, `PARTIAL` sau `LIPSEȘTE`
- card, cărți sau best practices inventate ca să închidă un rând
- `BOOK THEORY` cu număr de pagină sau fără sursa deschisă
- folder închis cât fișierul de topic sau o clasă din `fast` pică
- `_Staff_Interview` nou cu alt număr decât 50, sau o întrebare veche ștearsă ca să rămână 50
- FAQ cu corp umplut, sau întrebările de sub `Întrebări adăugate` șterse
- al doilea `*Fast.java`, sau un al doilea folder `fast`
- `*Brief.java` creat
- mecanism vecin lipsă, sau întrebare care merge neschimbată pe alt topic
- cheie dată la `learn` / `recall` înainte de încercare
- `MASTERED` sau `EXPERT` fără rând nou în `UTIL/student_state.md`
- manual sau PDF citat fără să fie deschis în tur
- PDF de 0 octeți citat
- copie a unui demo `ACOPERIT ÎN SRC`
- linie nouă fără comentariul detaliat, sau comentariu nou care repetă sintaxa
- orice ștergere de fișier, metodă, exemplu sau comentariu corect
- corecție fără `HISTORICAL` pe propoziția înlocuită
- `pom.xml` sau cardul `VERSION` rămase pe o versiune mai veche decât ultimul stabil citit în tur
- parolă sau URL de server inventate

## Raport

Doar când fiecare fișier din comandă a trecut poarta, inclusiv copiii și `fast`:

```markdown
## Raport final: <folder>
Release scris în pom: <java>
MySQL scris în pom: <versiune>
Connector/J scris în pom: <versiune>
| Fișier | Rezultat |
|---|---|
```

Rezultate: `VERIFIED`, `PARTIAL`, `PĂSTRAT`, `NERULAT AICI`. Raportul include tabela de acoperire și tabela de cărți. Nu declara familia gata după un singur copil. Fiecare fișier trece poarta înainte de linie. `PĂSTRAT` înseamnă neatins: fișierul nu a trecut poarta. Cât un fișier din comandă este `PĂSTRAT`, `PARTIAL` sau `LIPSEȘTE`, nu scrii `TASK TERMINAT 100%`.

După raport, verifici task-ul contra cerințelor din acest fișier. Dacă sincronizarea este 100%, ultima linie a răspunsului este exact `TASK TERMINAT 100%`. Dacă un punct nu ține, nu afișezi linia. Scrii `TASK TERMINAT 100%` doar după ce fiecare fișier din comandă a trecut poarta. Toate se actualizează, fără excepție. Un fișier lăsat neatins, `PĂSTRAT`, `PARTIAL` sau `LIPSEȘTE` oprește linia.

## Lecție rapidă

Se aplică la „explică-mi rapid”, „pe scurt” și „learn fast”, și la folderul `fast` din `update mysql`. Răspunde în limba în care scrie utilizatorul. La `learn`, `învață`, `recall`, `teach-back`, `mastery` și `exercițiu` cheia rămâne ascunsă până la încercare: acelea sunt MY.9, nu această secțiune.

Nu șterge ce există. Dacă clasa, metoda sau întrebarea este deja acolo și este corectă, o lași. Completezi ce lipsește. Un fapt greșit se corectează, iar propoziția veche rămâne marcată `HISTORICAL`.

- Efectul pe rânduri, schemă sau tranzacție înainte de lista de cuvinte cheie. Propoziția înainte de sinonime.
- Exemplul nou are cel mult ~15 linii de cod, compilează pe ultimul release Java scris în `pom.xml` în tur. Liniile de comentariu nu intră în cele 15. Propoziția este SQL pe care serverul îl poate rula.
- Fiecare linie de cod nouă are deasupra cele trei trepte din MY.7. O treaptă lipsă sau un comentariu care repetă sintaxa pică.
- Dacă în repo există deja un demo, exemplul pornește de la el. Nu inventa un al doilea vocabular.
- Un mecanism pe răspuns. Vecinul apare o dată, ca deosebire.

Răspunsul rapid, implicit: efectul într-o frază, propoziția minimă, vecinul, mini-exercițiul fără soluție. Complet, doar la cerere: și forțele, momentul în care se vede efectul, următorul mecanism. Soluția mini-exercițiului doar la cerere.

O lecție pe folderul din comandă, nu pe fiecare subfolder.

```text
_10_JOINs                         → fast/Joins_Fast.java
_10_JOINs/_1_INNER_JOIN           → fast/InnerJoin_Fast.java
_2_SQL_Statements/_2_DML/_1_SELECT/_2_WHERE → fast/Where_Fast.java
_2_SQL_Statements/_1_DDL/_5_TRUNCATE        → fast/Truncate_Fast.java
_8_Constraints                    → fast/Constraints_Fast.java
```

Dacă există deja un `*Fast.java`, îl completezi și îi păstrezi numele. Nu creezi al doilea. Nu creezi `*Brief.java`. Un `*Brief.java` deja prezent rămâne.

`<Stem>_Questions` este clasă goală la creare: patru până la șase întrebări în comentariu, despre acest mecanism, și `Întrebări adăugate` gol. Nu ștergi ce a scris utilizatorul acolo. `<Stem>_Staff_Interview` are la creare exact 50 de întrebări, distribuția din MY.6. Dacă are mai multe, le păstrezi. `<Stem>_Terms` stă în același folder `fast`. O definiție deja corectă rămâne.

Încheierea: o frază, ce mecanism s-a învățat și cu cine se confundă, plus un singur pas următor.
