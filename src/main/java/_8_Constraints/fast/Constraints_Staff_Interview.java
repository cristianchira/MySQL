/*
TOPIC:
    Cincizeci de întrebări despre regula pe care o alegi ca rândul de înscriere să rămână sau să cadă.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Recunoașterea din SQL, din eroare sau din EXPLAIN, momentul refuzului, deosebirea de index și de trigger, eșecul din INSERT IGNORE și alegerea față de aplicație.
MECHANISM:
    PRIMARY KEY pe (student_id, course_id) refuză perechea. FOREIGN KEY pe course_id refuză 404. Pe InnoDB propoziția se dă înapoi la sfârșitul ei. Indexul pe grade nu refuză.
LOOKALIKES:
    Trigger-ul rulează cod. Indexul secundar caută. NOT NULL refuză lipsa. DEFAULT umple coloana omisă. AUTO_INCREMENT generează numărul și nu este cheia.
BOOK THEORY:
    Manualul 9.7, 1.7.3, 1.7.3.1, 1.7.2.3, 15.1.25.5, 15.1.25.6, 5.6.9 și 17.6.1.6, deschise în tur. PDF-urile din folder, pe paginile deschise, nu descriu rollback-ul propoziției.
BOOK EXAMPLE:
    Probele din Constraints, nu o metodă din această clasă.
REPO DEMO:
    src/main/java/_8_Constraints/Constraints.java
BREAK LAB:
    INSERT IGNORE cu (1, 7, 'A') și (1, 7, 'B'), sau un trigger în loc de FOREIGN KEY.
TRADE-OFFS:
    INDEX când duplicatele sunt legale. UNIQUE când NULL trebuie să încapă. Aplicația când propoziția nu vede regula.
ANTI-PATTERNS:
    INSERT IGNORE pe perechea repetată, foreign_key_checks=0 lăsat pornit la scris, trigger în loc de FOREIGN KEY.
STAFF QUESTIONS:
    Întrebările 1–50 de mai jos. Cheia este scrisă. Nu există main care să o citească singur.
NOVEL TRANSFER:
    Coletul pe raft: o dată pe raft, raftul trebuie să existe, culoarea poate reveni.
SPACED REVIEW:
    O zi: întrebările 11–20. Șapte zile: 31–40. Treizeci de zile: 41–50.

Definiția unei constrângeri (constraint):
       Constrângerea este regula verificată de server la propoziția care scrie rândul. Rândul care o încalcă nu rămâne.

Exemplu:
       Constraints_Terms.explainConstraint()

Definiția unui index (index):
       Indexul secundar fără UNIQUE nu refuză valoarea repetată. Cheia primară este indexul care refuză perechea.

Exemplu:
       Constraints_Terms.explainIndex()

Recunoaștere din SQL, EXPLAIN sau eroare:
    1. Enunț: În CREATE TABLE course_registration vezi PRIMARY KEY (student_id, course_id) și grade CHAR(1) NULL. Ce refuză cheia?
       Capcană: Cheia refuză și nota repetată, fiindcă stă pe tabelă.
       Cheie: Refuză a doua pereche student-curs. grade poate fi NULL și se poate repeta. Nota repetată ar cădea doar cu UNIQUE sau PRIMARY KEY pe grade, care nu este în propoziție.
    2. Enunț: Clientul primește ERROR 1062 (23000) Duplicate entry. Ce regulă a căzut?
       Capcană: Este 1452, deci cursul 404 lipsește.
       Cheie: 1062 este perechea sau valoarea unică deja prezentă. 1452 este părintele lipsă. NOT NULL lipsă este 1048, nu 1062.
    3. Enunț: Ce recunoști în ERROR 1452 (23000) Cannot add or update a child row, pe course_registration?
       Capcană: Cheia primară compusă s-a repetat.
       Cheie: Copilul arată spre un course.id care nu există, sau părintele nu poate fi șters cât copilul îl ține. Nu este duplicatul perechii.
    4. Enunț: ERROR 1048 (23000) Column 'student_id' cannot be null. Ce coloană a fost judecată?
       Capcană: DEFAULT 0 ar fi trebuit să umple student_id, deci regula care a căzut este DEFAULT.
       Cheie: student_id este NOT NULL și a primit NULL. DEFAULT nu umple un NULL scris explicit. 1048 nu este 1364.
    5. Enunț: ERROR 1364 (HY000) Field 'student_id' doesn't have a default value. Ce s-a omis?
       Capcană: Ai trimis NULL și NOT NULL l-a refuzat cu 1048.
       Cheie: Coloana nu este în lista INSERT-ului, este NOT NULL și nu are DEFAULT. În modul strict, pe InnoDB, propoziția se oprește. Nu s-a inventat un NULL.
    6. Enunț: EXPLAIN pe o căutare după grade arată key: i_grade. Ce ai dovedit despre constrângere?
       Capcană: i_grade este PRIMARY KEY, deci a doua notă A nu poate fi scrisă.
       Cheie: Planul a folosit indexul secundar. Nu ai dovedit un refuz. Două rânduri cu grade A rămân dacă i_grade nu este UNIQUE.
    7. Enunț: Ce recunoști în CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE RESTRICT?
       Capcană: Este un trigger BEFORE DELETE care șterge înscrierile.
       Cheie: Este regula de pe copil. Ștergerea cursului cădea cât există o înscriere. RESTRICT nu este CASCADE și nu este cod de trigger.
    8. Enunț: SHOW WARNINGS după INSERT IGNORE arată Duplicate entry. Ce rând a rămas?
       Capcană: Niciun rând, fiindcă IGNORE anulează toată propoziția.
       Cheie: Perechea care a încălcat PRIMARY KEY sau UNIQUE a fost sărită. Rândurile dinaintea ei, din aceeași propoziție, pot rămâne. Nu este rollback-ul lui 1452.
    9. Enunț: CREATE TABLE cade pentru că AUTO_INCREMENT nu este prima coloană în niciun index, pe InnoDB. Ce ai cerut?
       Capcană: AUTO_INCREMENT nu poate sta lângă o cheie compusă, în nicio formă.
       Cheie: Pe InnoDB coloana AUTO_INCREMENT trebuie să fie prima sau singura dintr-un index. O cheie compusă în care ea este prima coloană este legală. Interdicția absolută nu este regula din manualul 9.7.
    10. Enunț: Ce recunoști în CHECK (grade IN ('A','B','C','D','F')) NOT ENFORCED?
        Capcană: Nota G este respinsă, fiindcă expresia este scrisă.
        Cheie: Constrângerea este creată și nu este aplicată. G rămâne. Fără NOT ENFORCED, expresia FALSE ar opri propoziția. Acest folder nu are un copil CHECK.

Rânduri, index, lacăt și momentul în care se vede efectul:
    11. Enunț: Pe InnoDB, INSERT-ul cu course_id 404 încalcă FOREIGN KEY. Când vezi că rândul nu a rămas?
        Capcană: La COMMIT. Până atunci rândul este în tranzacție și poate fi citit de alt client.
        Cheie: La sfârșitul propoziției. Propoziția se dă înapoi. Verificarea nu este amânată până la commit, spre deosebire de standardul care amână implicit.
    12. Enunț: InnoDB examinează părintele la FOREIGN KEY. Ce lacăt pune și cât ține?
        Capcană: Lacăt exclusiv pe toată tabela course, până la COMMIT.
        Cheie: Lacăt partajat pe înregistrările părinte sau copil examinate, pe durata verificării. Nu ține locul regulii și nu rămâne după ce eroarea a plecat la client.
    13. Enunț: Scrii două înscrieri cu același student și curs, fără IGNORE, pe InnoDB. Ce rămâne din propoziție?
        Capcană: Prima rămâne, a doua pică, ca pe un motor care nu poate da propoziția înapoi.
        Cheie: Pe InnoDB toată propoziția se dă înapoi. Pe un motor netranzacțional, serverul se oprește la rândul greșit și lasă rândurile dinaintea lui.
    14. Enunț: UNIQUE (grade) și două înscrieri cu grade NULL. Câte rânduri rămân pe InnoDB?
        Capcană: Al doilea NULL este duplicat, ca la PRIMARY KEY.
        Cheie: Mai multe NULL sunt permise pe UNIQUE în InnoDB. PRIMARY KEY nu acceptă NULL. Slide-ul NOT NULL deschis spune același contrast: unique key yes, primary key no, la NULL VALUES.
    15. Enunț: INSERT omite grade, iar coloana este CHAR(1) NULL DEFAULT 'N'. Ce valoare rămâne?
        Capcană: Rămâne NULL, fiindcă nu ai scris nota.
        Cheie: DEFAULT umple coloana omisă, aici 'N'. NULL scris explicit nu este înlocuit de DEFAULT.
    16. Enunț: Când vezi numărul generat de AUTO_INCREMENT pe id-ul din course?
        Capcană: La COMMIT, și LAST_INSERT_ID() îl vede orice conexiune.
        Cheie: La propoziția care a generat rândul. LAST_INSERT_ID() este pe conexiunea aceasta și, la un INSERT cu mai multe rânduri, întoarce prima valoare generată în propoziție.
    17. Enunț: SET foreign_key_checks=0, apoi INSERT cu 404, apoi iar 1. Rândul cu 404 mai este verificat?
        Capcană: La repornire, serverul scanează tabela și șterge orfanul.
        Cheie: Nu. Repornirea variabilei nu scanează rândurile scrise cât verificarea era oprită. Orfanul rămâne.
    18. Enunț: DELETE FROM course WHERE id=7, iar o înscriere ține course_id 7 cu ON DELETE RESTRICT. Ce dispare?
        Capcană: Cursul dispare, iar înscrierea rămâne cu 7, fiindcă RESTRICT se uită doar la INSERT.
        Cheie: Ștergerea părintelui este respinsă. Nici cursul, nici înscrierea nu pleacă. CASCADE ar fi șters înscrierile în aceeași propoziție.
    19. Enunț: Un index secundar InnoDB pe grade ce păstrează lângă notă?
        Capcană: Doar grade, ca o copie a coloanei, fără legătură cu perechea.
        Cheie: În InnoDB, indexul secundar ține și cheia primară. Tot nu refuză a doua notă A. Refuzul perechii este PRIMARY KEY.
    20. Enunț: ALTER TABLE course AUTO_INCREMENT=1, iar există deja id 40. Următorul INSERT fără id ce număr primește?
        Capcană: 1, fiindcă ai resetat contorul.
        Cheie: Următorul număr nu coboară sub maximul existent. 1 nu reapare cât 40 este în tabelă. TRUNCATE este alt drum și golește tabela.

Deosebire de mecanismul vecin:
    21. Enunț: De ce CREATE TRIGGER registration_course_check nu este FOREIGN KEY?
        Capcană: SIGNAL SQLSTATE '45000' este aceeași eroare 1452, deci regula este echivalentă.
        Cheie: Trigger-ul rulează codul tău. FOREIGN KEY este regula serverului, cu lacăt pe înregistrările examinate și cu ON DELETE. Trigger-ul nu ține locul cheii străine.
    22. Enunț: De ce CREATE INDEX i_grade nu este UNIQUE (grade)?
        Capcană: Orice index este o constrângere, fiindcă stă în folderul _8_Constraints.
        Cheie: i_grade nu refuză a doua notă A. UNIQUE refuză. Folderul nu schimbă propoziția.
    23. Enunț: De ce NOT NULL pe student_id nu este DEFAULT 0?
        Capcană: Ambele umplu coloana când o uiți, deci sunt aceeași regulă.
        Cheie: NOT NULL refuză lipsa și refuză NULL explicit. DEFAULT pune 0 doar când INSERT-ul omite coloana. NULL scris explicit rămâne NULL dacă coloana îl acceptă.
    24. Enunț: De ce AUTO_INCREMENT pe course.id nu este PRIMARY KEY?
        Capcană: Numărul generat este unica regulă, deci cheia primară este de prisos.
        Cheie: AUTO_INCREMENT generează numărul. PRIMARY KEY refuză duplicatul și NULL. Pot sta împreună. Fără un index în care coloana este prima, InnoDB nu acceptă AUTO_INCREMENT.
    25. Enunț: De ce ENUM('A','B','C') pe grade nu este CHECK (grade IN ('A','B','C'))?
        Capcană: Ambele sunt aceeași listă, deci același refuz, în orice sql_mode.
        Cheie: ENUM este tipul coloanei. CHECK este regula de tabelă sau de coloană, iar expresia FALSE oprește propoziția când este ENFORCED. Modul SQL poate coerționa un ENUM invalid dacă strict mode nu este pornit. Nu sunt aceeași propoziție.
    26. Enunț: De ce PRIMARY KEY (id) nu este un index secundar pe id?
        Capcană: Ambele găsesc rândul, deci planul și refuzul sunt aceleași.
        Cheie: În InnoDB, PRIMARY KEY este indexul grupat, unul singur, și nu acceptă NULL. Indexul secundar ține cheia primară și nu este identitatea tabelei.
    27. Enunț: De ce un WHERE course_id=404 într-un SELECT nu apără următorul INSERT?
        Capcană: Dacă SELECT-ul nu găsește 404, INSERT-ul este deja imposibil.
        Cheie: WHERE filtrează citirea. Nu rămâne ca regulă în schemă. Următorul INSERT poate scrie 404 dacă nu ai FOREIGN KEY.
    28. Enunț: De ce o verificare în aplicație, înainte de INSERT, nu este constrângerea?
        Capcană: Aplicația vede același tabel, deci al doilea client nu poate intra.
        Cheie: Doi clienți pot trece amândoi de verificare. Refuzul perechii este lacătul și regula de pe indexul unic, la propoziție, nu if-ul din proces.
    29. Enunț: De ce foreign_key_checks=0 nu este DROP FOREIGN KEY?
        Capcană: Oprit înseamnă că regula a fost ștearsă din schemă.
        Cheie: Variabila sare verificarea la DML pe sesiune sau global. Definiția rămâne. DROP scoate regula. La repornirea variabilei, orfanii scriși între timp nu sunt scanați.
    30. Enunț: De ce CHECK (grade <> '') nu este NOT NULL pe grade?
        Capcană: Ambele opresc nota lipsă, deci sunt aceeași regulă.
        Cheie: NOT NULL refuză NULL. Șirul gol nu este NULL și trece de NOT NULL. CHECK pe expresie refuză FALSE, iar NULL în expresie este UNKNOWN și nu este eșec. Folderul nu predă CHECK până la capăt.

Eșec în producție:
    31. Enunț: INSERT IGNORE scrie (1, 7, 'A') și (1, 7, 'B'). Ce notă rămâne la studentul 1, cursul 7?
        Capcană: 'B', fiindcă a doua valoare o înlocuiește pe prima.
        Cheie: 'A', dacă prima pereche a intrat. A doua este sărită. Fără citirea avertismentului, clientul crede că ambele note s-au păstrat.
    32. Enunț: Lași foreign_key_checks=0 pe sesiunea care încarcă un dump, apoi uiți să-l pui la 1 și aplici încă un INSERT cu 404. Ce rămâne?
        Capcană: Doar dump-ul a fost iertat. INSERT-ul următor este verificat, fiindcă regula este în CREATE.
        Cheie: Cât variabila este 0, și INSERT-ul de după sare verificarea. 404 rămâne. Repornirea de mai târziu nu îl șterge.
    33. Enunț: FOREIGN KEY spre o coloană care nu este UNIQUE, permisă doar cu restrict_fk_on_non_standard_key. De ce este o problemă?
        Capcană: Este forma din standard și rămâne suportată.
        Cheie: Este o extensie InnoDB, depreciată. Mai mulți părinți cu aceeași valoare nu sunt o cheie. Trebuie migrat la o cheie unică sau primară.
    34. Enunț: ON UPDATE CASCADE pe o cheie care se întoarce în aceeași tabelă. Ce face al doilea pas care ar atinge același rând?
        Capcană: Cascada continuă până nu mai rămâne nimic de actualizat.
        Cheie: Dacă se întoarce la un rând deja actualizat în aceeași cascadă, se comportă ca RESTRICT. Nu este o cascadă infinită. Adâncimea cascadelor nu trece de 15.
    35. Enunț: Pui MATCH SIMPLE sau FULL în FOREIGN KEY, ca să controlezi NULL pe coloanele compuse. Ce se întâmplă cu ON DELETE?
        Capcană: MATCH schimbă doar NULL-ul, iar ON DELETE CASCADE rămâne.
        Cheie: Niciun motor, inclusiv InnoDB, nu aplică MATCH. Clauza explicită face ca ON DELETE și ON UPDATE să fie ignorate. Nu o scrie.
    36. Enunț: Ștergi un rând care se referă pe el însuși prin FOREIGN KEY, într-o singură propoziție. De ce cade?
        Capcană: Cade doar dacă ai uitat ON DELETE CASCADE.
        Cheie: Verificarea este imediată, pe rând, nu la sfârșitul propoziției în sensul amânat al standardului. Nu poți șterge astfel un rând care încă se vede pe el însuși prin regulă.
    37. Enunț: sql_mode fără STRICT_TRANS_TABLES, pe un motor netranzacțional, și o valoare care nu încape în coloană. Ce rămâne în rând?
        Capcană: Propoziția cade, ca pe InnoDB în modul strict din 9.7.
        Cheie: Serverul poate coerționa la cea mai apropiată valoare, scrie rândul și dă avertisment. Modul implicit din 9.7 include STRICT_TRANS_TABLES; fără el, rândul stricat poate rămâne.
    38. Enunț: DROP PRIMARY KEY pe (student_id, course_id). Ce se întâmplă cu student_id dacă voiai să rămână singur în cheie?
        Capcană: Cade doar course_id. student_id rămâne cheie.
        Cheie: Cade toată cheia compusă. Nu există drop pe o singură coloană din ea. O pui la loc cu coloanele pe care le mai vrei.
    39. Enunț: Două tabele din aceeași bază primesc CHECK cu același nume, chk_grade. Ce cade?
        Capcană: Numele trebuie să fie unic doar în tabelă, ca la un index.
        Cheie: Numele de CHECK este unic în schemă, nu doar în tabelă. A doua tabelă nu poate reutiliza chk_grade. PRIMARY KEY și CHECK nu împart același spațiu de nume în MySQL.
    40. Enunț: Un INSERT cu mai multe rânduri încalcă CHECK la al treilea rând, pe InnoDB, în modul strict. Ce rămâne din primele două?
        Capcană: Primele două rămân, fiindcă verificarea este pe rând și rândurile bune au fost scrise.
        Cheie: Pe tabela tranzacțională, în modul strict, propoziția se oprește și se dă înapoi. Primele două nu rămân. INSERT IGNORE ar sări rândul care eșuează și ar putea păstra celelalte.

Alegere, inclusiv când rămâi în aplicație:
    41. Enunț: Nota trebuie să existe într-un catalog din afara lui MySQL, pe care propoziția nu îl poate citi. Unde stă regula?
        Capcană: Într-un trigger cu subcerere spre catalogul extern, fiindcă trigger-ul este constrângerea.
        Cheie: În aplicație, sau într-un serviciu pe care îl cheamă aplicația. Serverul nu vede catalogul. FOREIGN KEY vede doar o tabelă din acest server.
    42. Enunț: Tabela are deja PRIMARY KEY (student_id, course_id). Vrei ca emailul studentului, când este prezent, să nu se repete, dar lipsa lui să fie permisă. Ce alegi?
        Capcană: A doua PRIMARY KEY pe email.
        Cheie: UNIQUE pe email. O tabelă are o singură cheie primară. UNIQUE pe InnoDB acceptă mai multe NULL. A doua PRIMARY KEY nu există.
    43. Enunț: Cauți des după grade și două înscrieri pot avea A. Ce pui?
        Capcană: PRIMARY KEY (grade), ca planul să fie sigur.
        Cheie: Un index secundar pe grade. PRIMARY KEY ar refuza a doua notă A și ar schimba identitatea tabelei. Indexul nefolosit nu grăbește propoziția; ăsta este alt folder, de plan.
    44. Enunț: student_id omis trebuie să oprească INSERT-ul. grade omis trebuie să devină 'N'. Ce pui pe fiecare?
        Capcană: NOT NULL pe ambele, fiindcă ambele sunt importante.
        Cheie: student_id NOT NULL, fără DEFAULT. grade DEFAULT 'N'. NOT NULL fără DEFAULT oprește omisiunea în modul strict. DEFAULT umple doar omisiunea.
    45. Enunț: Ștergerea unui curs nu are voie să lase înscrieri, iar înscrierile nu au sens fără curs. Ce acțiune scrii?
        Capcană: ON DELETE SET NULL, ca să păstrezi istoricul notei cu course_id gol.
        Cheie: ON DELETE CASCADE dacă înscrierea trebuie să plece odată cu cursul. RESTRICT dacă ștergerea cursului trebuie să cadă cât există înscrieri. SET NULL păstrează înscrierea orfană, cu course_id NULL, și cere ca course_id să accepte NULL.
    46. Enunț: Nota trebuie să fie una din A, B, C, D, F, iar NULL este permis cât nu s-a dat nota. Ce nu ajunge?
        Capcană: NOT NULL, fiindcă lista este o regulă de coloană.
        Cheie: NOT NULL ar interzice și lipsa notei. Lista este o expresie CHECK, sau un ENUM dacă accepti regulile tipului. Acest folder nu predă expresia până la capăt. Trigger-ul nu este înlocuitorul.
    47. Enunț: Doi clienți înscriu același student la același curs în același moment. Unde trebuie să cadă al doilea?
        Capcană: În if-ul din aplicație, fiindcă a citit tabela înainte de INSERT.
        Cheie: La PRIMARY KEY, pe server, la propoziție. Citirea din aplicație nu vede intenția celuilalt client. Indexul unic este locul unde a doua pereche cade.
    48. Enunț: course.id este un număr pe care îl dai tu, dintr-un registru deja stabil. Mai pui AUTO_INCREMENT?
        Capcană: Da, altfel PRIMARY KEY nu are valoare.
        Cheie: Nu. PRIMARY KEY (id) ajunge. AUTO_INCREMENT generează când tu nu trimiți numărul. Un număr din registru scris explicit nu are nevoie de generator.
    49. Enunț: Vrei ca încălcarea să nu lase în urmă primele rânduri ale unui INSERT cu mai multe rânduri. Ce motor ceri?
        Capcană: MyISAM, fiindcă este mai simplu și oprește curat la rândul greșit.
        Cheie: InnoDB. Propoziția se dă înapoi. MyISAM nu ține tranzacția și nu ține FOREIGN KEY. Oprirea la rândul greșit lasă rândurile dinaintea lui.
    50. Enunț: Un colet de depozit nu poate sta de două ori pe același raft, raftul trebuie să existe, iar două colete pot avea aceeași culoare. Ce este fiecare regulă?
        Capcană: Toate trei sunt PRIMARY KEY, fiindcă toate se caută des.
        Cheie: Perechea colet-raft este PRIMARY KEY sau UNIQUE. Raftul este FOREIGN KEY. Culoarea este un index secundar, fără UNIQUE, dacă o cauți. Culoarea nu este constrângerea care refuză.

🟢 Începător
    Ce este            banca de interviu pentru alegerea regulii, cu cheia scrisă
    De ce e nevoie     fără cele cincizeci, recunoașterea din 1452 și din 1062 rămâne una singură
🟡 Producție
    Cum se scrie       cincizeci de enunțuri în comentariu, fără main
🔴 Staff
    Unde               cheia stă aici. Proba care ține SQL-ul stă în Constraints. Această clasă nu o trimite.
Pași
    1. Citește enunțul.
    2. Cheia este deja scrisă, pentru artefact.
    3. Nu porni un main de recall.
*/
// Începător: banca stă lângă lecția scurtă și nu înscrie un student.
// Producție: package _8_Constraints.fast. Nu are main de recall.
// Expert: Cele cincizeci sunt despre alegerea regulii pe course_registration. Nu se lipesc pe WHERE sau pe JOIN.
package _8_Constraints.fast;

// Începător: clasa ține întrebările și cheile în comentariul de deasupra.
// Producție: public final class Constraints_Staff_Interview. Dacă primește main, nu mai este banca fără recall.
// Expert: Nu execută INSERT-ul cu 404. Efectul pe server rămâne NERULAT în tur.
public final class Constraints_Staff_Interview {
    // Începător: nu există o propoziție în corp.
    // Producție: acolada închide Constraints_Staff_Interview. Sub ea nu se adaugă un main.
    // Expert: Cele cincizeci rămân în comentariu. Golul nu șterge o cheie.
}
