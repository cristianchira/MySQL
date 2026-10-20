package _14_VIEW.fast;

import _14_VIEW.DB_VIEW;

/*
TOPIC:
    Banca întreabă cine ține rândul când propoziția este CREATE VIEW pe dept_emp.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost cu parolă goală a primit Access denied (using password: NO). Nu există parolă în repo. Efectul pe server rămâne NERULAT AICI.
INTENT:
    După propoziția corectă există v_dept_emp_latest_date, iar emp_no 10001 rămâne în dept_emp.
MECHANISM:
    mysqld salvează SELECT-ul, face commit implicit la CREATE VIEW și respinge INSERT-ul în view-ul cu GROUP BY.
LOOKALIKES:
    Tabela dept_emp. Se separă prin cine ține rândul: tabela îl ține, view-ul îl recalculează.
BOOK THEORY:
    Manualul 9.7: definiția îngheață, IS_UPDATABLE se fixează la creare, TEMPTABLE nu este updatable, WITH CHECK OPTION păzește WHERE-ul, CREATE VIEW și DROP VIEW fac commit implicit. SQL+Views.pdf nu spune commit-ul și interzice orice scriere.
BOOK EXAMPLE:
    DB_VIEW.createLatestDateView()
REPO DEMO:
    src/main/java/_14_VIEW/DB_VIEW.java
BREAK LAB:
    INSERT în v_dept_emp_latest_date este respins. UPDATE-ul care pune to_date = 2025-06-05 pe v_dept_emp_open este respins de CHECK OPTION.
TRADE-OFFS:
    Tabelă derivată fără commit implicit. Tabelă reală când agregatul trebuie stocat. v_dept_emp_open când scrierea schimbă dept_emp.
ANTI-PATTERNS:
    INSERT în view-ul agregat, sau LIMIT 5 peste LIMIT 10 din definiție.
STAFF QUESTIONS:
    recognizeStoredQuery(), effectOnBaseRows(), separateFromBaseTable(), failOnInsert(), chooseOpenView()
NOVEL TRANSFER:
    Registru de ture: v_shift_latest pe shift_log, fereastră cu ended_at IS NULL.
SPACED REVIEW:
    O zi, șapte zile, treizeci de zile: rândul, INSERT-ul, commit-ul implicit.
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE salvezi agregatul -> recognizeStoredQuery() -> CREATE OR REPLACE VIEW -> nu copiază dept_emp
    2. Eșecul cel mai mic
           -> DECIDE INSERT în agregat -> failOnInsert() -> scrierea este respinsă
    3. Granița
           -> banca nu este mecanismul; propoziția este a serverului
    4. Alternativa
           -> chooseOpenView() când UPDATE-ul trebuie să schimbe dept_no
    5. Anti-patternul
           -> INSERT în v_dept_emp_latest_date
    6. Dovada
           -> metodele de mai jos întorc șirurile din DB_VIEW
    7. Interviu
           -> view-ul salvează SELECT-ul; tabela ține rândul; agregatul nu primește INSERT

Definiția unei vederi (view):
       View-ul este interogarea salvată. Rândurile rămân în dept_emp.

Exemplu:
       View_Terms.explainView()
       v_dept_emp_latest_date nu copiază rândul 10001.

Definiția unei tabele de bază (base table):
       Tabela ține rândurile și nu poate avea același nume cu view-ul.

Exemplu:
       View_Terms.explainBaseTable()
       DROP VIEW lasă 10001 în dept_emp.

Definiția unei coloane de view (view column):
       Coloanele se îngheață la CREATE VIEW.

Exemplu:
       View_Terms.explainViewColumn()
       max_date nu este o coloană stocată.

Definiția unui index folosit prin îmbinare (MERGE index):
       Indexul care poate intra în plan este al lui dept_emp, și doar la MERGE.

Exemplu:
       View_Terms.explainMergeIndex()
       Acest view are GROUP BY și LIMIT, deci nu se îmbină.

Definiția unui lacăt eliberat la tabela temporară (TEMPTABLE lock):
       TEMPTABLE poate elibera lacătele de pe dept_emp după umplere.

Exemplu:
       View_Terms.explainTemptableLock()
       View-ul agregat nu este updatable.

Definiția unei tranzacții întrerupte de view (implicit commit):
       CREATE VIEW face commit implicit. UPDATE-ul prin fereastra updatable nu.

Exemplu:
       View_Terms.explainImplicitCommit()
       CREATE OR REPLACE VIEW închide tranzacția deschisă.
*/

/*
Recunoaștere din SQL, din steag sau din refuz. 10.
1. Enunț: Vezi CREATE OR REPLACE VIEW v_dept_emp_latest_date AS SELECT emp_no, MAX(from_date) AS max_date, MIN(from_date) AS min_date FROM dept_emp GROUP BY emp_no LIMIT 10. Ce obiect apare?
   Capcană: „Este o tabelă cu zece rânduri copiate din dept_emp.”
   Cheie: Este un view. Serverul salvează SELECT-ul. Rândurile rămân în dept_emp.
   Demonstrație: View_Staff_Interview.recognizeStoredQuery()
2. Enunț: SELECT * FROM v_dept_emp_latest_date. De unde vin coloanele max_date și min_date?
   Capcană: „Sunt coloane stocate în view, umplute la CREATE VIEW.”
   Cheie: Sunt aliasurile lui MAX(from_date) și MIN(from_date), recalculate la acest SELECT.
   Demonstrație: View_Staff_Interview.effectOnBaseRows()
3. Enunț: INSERT INTO v_dept_emp_latest_date (emp_no, max_date, min_date) VALUES (10001, '1986-06-26', '1986-06-26'). Ce se scrie?
   Capcană: „Grupul 10001 este inserat în dept_emp.”
   Cheie: View-ul are agregat, GROUP BY și LIMIT. Scrierea este respinsă. dept_emp nu primește rândul pe această cale.
   Demonstrație: View_Staff_Interview.failOnInsert()
4. Enunț: SELECT IS_UPDATABLE pentru v_dept_emp_latest_date. Când a fost fixat semnul?
   Capcană: „Serverul îl calculează la primul INSERT, ca să vadă dacă trece.”
   Cheie: IS_UPDATABLE se fixează la CREATE VIEW. Pentru acest view este NO.
   Demonstrație: View_Staff_Interview.updatableFlag()
5. Enunț: CREATE VIEW v_dept_emp_latest_date ... urmat de același CREATE VIEW, fără OR REPLACE și fără IF NOT EXISTS. Ce răspuns ai văzut în manual pentru un view deja prezent?
   Capcană: „Al doilea CREATE îl înlocuiește în tăcere.”
   Cheie: Manualul arată ERROR 1050 (42S01): Table 'v1' already exists. IF NOT EXISTS lasă definiția neschimbată și dă Note 1050. OR REPLACE chiar o înlocuiește.
   Demonstrație: View_Staff_Interview.recognizeStoredQuery()
6. Enunț: CREATE OR REPLACE VIEW ... IF NOT EXISTS pe același nume. Ce face serverul?
   Capcană: „Le combină: înlocuiește dacă există, altfel creează.”
   Cheie: OR REPLACE și IF NOT EXISTS se exclud. Propoziția este respinsă ca sintaxă.
   Demonstrație: View_Staff_Interview.recognizeStoredQuery()
7. Enunț: Algoritmul cerut este MERGE, dar view-ul are GROUP BY și LIMIT. Ce algoritm rămâne?
   Capcană: „MERGE forțează îmbinarea și face view-ul updatable.”
   Cheie: MERGE nu se poate. Serverul avertizează și lasă algoritmul UNDEFINED, iar procesarea cade pe tabelă temporară. View-ul nu este updatable.
   Demonstrație: View_Staff_Interview.recognizeStoredQuery()
8. Enunț: În definiție apare ALGORITHM = TEMPTABLE, fără agregat. UPDATE-ul prin view trece?
   Capcană: „TEMPTABLE doar grăbește citirea, scrierea rămâne.”
   Cheie: Tabela temporară face view-ul non-updatable. UPDATE-ul este respins.
   Demonstrație: View_Staff_Interview.failOnInsert()
9. Enunț: CREATE VIEW ... MATERIALIZED pe serverul 9.7, în afara HeatWave. S-a stocat rezultatul?
   Capcană: „MATERIALIZED este sinonim cu TEMPTABLE și ține grupurile în view.”
   Cheie: Manualul limitează view-ul materializat la HeatWave. TEMPTABLE nu păstrează rezultatul după propoziție.
   Demonstrație: View_Staff_Interview.separateFromBaseTable()
10. Enunț: SELECT * FROM v_dept_emp_latest_date LIMIT 5, iar definiția are LIMIT 10. Câte grupuri garantează manualul?
    Capcană: „Cinci, pentru că limita exterioară înlocuiește limita din view, ca ORDER BY.”
    Cheie: Două clauze LIMIT lasă nedefinit care se aplică. ORDER BY exterior chiar înlocuiește ORDER BY din view. Aici nu ai acel înlocuitor.
    Demonstrație: View_Staff_Interview.undefinedLimit()

Rânduri, index, lacăt și momentul în care se vede efectul. 10.
11. Enunț: Imediat după CREATE OR REPLACE VIEW v_dept_emp_latest_date, unde este rândul cu emp_no 10001?
    Capcană: „A fost mutat în view și dept_emp nu îl mai are.”
    Cheie: Rămâne în dept_emp. View-ul nu primește o copie.
    Demonstrație: View_Staff_Interview.effectOnBaseRows()
12. Enunț: Schimbi from_date în dept_emp după ce view-ul există. Când se vede data nouă în v_dept_emp_latest_date?
    Capcană: „La COMMIT-ul care a urmat lui CREATE VIEW, pentru că view-ul a fotografiat data.”
    Cheie: La sfârșitul următorului SELECT din view. Serverul reexecută agregatul. CREATE VIEW a făcut deja commit-ul lui implicit și nu a ținut data.
    Demonstrație: View_Staff_Interview.effectOnBaseRows()
13. Enunț: DROP VIEW v_dept_emp_latest_date. Ce se întâmplă cu rândurile din dept_emp?
    Capcană: „Dispar odată cu view-ul, pentru că view-ul era tabela lor.”
    Cheie: Obiectul view dispare. Rândurile rămân în dept_emp. DROP VIEW face commit implicit.
    Demonstrație: View_Staff_Interview.separateFromBaseTable()
14. Enunț: O tranzacție este deschisă și urmează CREATE VIEW. Ce se întâmplă cu schimbările dinaintea lui?
    Capcană: „Rămân necomise până la COMMIT-ul tău, iar view-ul intră în aceeași tranzacție.”
    Cheie: CREATE VIEW este în lista de commit implicit, înainte și după propoziție. Tranzacția deschisă se închide.
    Demonstrație: View_Staff_Interview.recognizeStoredQuery()
15. Enunț: UPDATE v_dept_emp_open SET dept_no = 'd005' WHERE emp_no = 10001, într-o tranzacție deschisă. Cine face commit?
    Capcană: „Orice propoziție care numește un view face commit implicit.”
    Cheie: UPDATE-ul nu este în lista de commit implicit. dept_no se schimbă în dept_emp la sfârșitul UPDATE-ului și rămâne în tranzacția curentă.
    Demonstrație: View_Staff_Interview.writeDepartment()
16. Enunț: Vrei un index pe max_date ca să grăbești citirea view-ului agregat. Unde îl pui?
    Capcană: „CREATE INDEX pe v_dept_emp_latest_date, pentru că view-ul ține grupurile.”
    Cheie: View-ul nu are depozit de rânduri. Indexul stă pe dept_emp. Acest view nici nu se îmbină, din cauza lui GROUP BY și LIMIT.
    Demonstrație: View_Staff_Interview.separateFromBaseTable()
17. Enunț: La o citire cu TEMPTABLE, cât stau lacătele pe dept_emp?
    Capcană: „Cât există view-ul în schemă.”
    Cheie: Pot fi eliberate după ce tabela temporară a fost umplută și înainte ca propoziția să se termine. La MERGE rămân pe durata propoziției îmbinate.
    Demonstrație: View_Staff_Interview.effectOnBaseRows()
18. Enunț: Adaugi o coloană în dept_emp după CREATE VIEW v_dept_emp_latest_date. SELECT * din view o arată?
    Capcană: „View-ul este dinamic și preia structura nouă la următoarea citire.”
    Cheie: Definiția este înghețată. Coloana nouă nu intră. Dacă ștergi o coloană folosită de view, citirea eșuează.
    Demonstrație: View_Staff_Interview.effectOnBaseRows()
19. Enunț: ALTER TABLE pe dept_emp, apoi citirea view-ului care încă numește coloana ștearsă. Când vezi eroarea?
    Capcană: „La ALTER TABLE, pentru că view-ul se recompilează singur.”
    Cheie: La SELECT-ul din view. CHECK TABLE pe view caută definiția stricată. Crearea nu s-a schimbat singură.
    Demonstrație: View_Staff_Interview.effectOnBaseRows()
20. Enunț: UPDATE-ul prin v_dept_emp_open schimbă dept_no. Unde este valoarea nouă la sfârșitul propoziției?
    Capcană: „Doar în view, până copiază cineva în dept_emp.”
    Cheie: În rândul din dept_emp. View-ul nu ține o copie. to_date a rămas 9999-01-01, deci CHECK OPTION a lăsat scrierea.
    Demonstrație: View_Staff_Interview.writeDepartment()

Deosebire de tabelă. 10.
21. Enunț: De ce v_dept_emp_latest_date nu este dept_emp?
    Capcană: „Ambele se citesc cu SELECT, deci sunt același fel de tabelă.”
    Cheie: Tabela ține rândurile. View-ul este interogarea salvată. Nu pot purta același nume.
    Demonstrație: View_Staff_Interview.separateFromBaseTable()
22. Enunț: CREATE VIEW dept_emp AS SELECT emp_no FROM dept_emp. Ce oprește propoziția înainte de agregat?
    Capcană: „Serverul redenumește singur view-ul.”
    Cheie: Tabela și view-ul împart spațiul de nume. Numele dept_emp este ocupat de tabelă.
    Demonstrație: View_Staff_Interview.separateFromBaseTable()
23. Enunț: Același SELECT cu GROUP BY stă în FROM ca tabelă derivată, nu ca view. Ce nu se întâmplă?
    Capcană: „Este același obiect, doar fără nume, și tot face commit implicit.”
    Cheie: Tabela derivată trăiește o propoziție. Nu intră în schemă și nu este CREATE VIEW, deci nu închide tranzacția prin lista lui CREATE VIEW.
    Demonstrație: View_Staff_Interview.recognizeStoredQuery()
24. Enunț: Poți pune un trigger BEFORE INSERT pe v_dept_emp_open?
    Capcană: „View-ul updatable este o tabelă, deci primește trigger.”
    Cheie: Nu poți asocia un trigger cu un view. Regula de fereastră este WITH CHECK OPTION, nu trigger-ul.
    Demonstrație: View_Staff_Interview.chooseOpenView()
25. Enunț: CREATE TEMPORARY VIEW v_dept_emp_latest_date. Ce obiect temporar apare?
    Capcană: „Un view de sesiune, analog cu tabela temporară.”
    Cheie: Nu există TEMPORARY VIEW. View-ul nu poate nici referi o tabelă TEMPORARY.
    Demonstrație: View_Staff_Interview.recognizeStoredQuery()
26. Enunț: CALL unei proceduri care face același SELECT. Este view-ul?
    Capcană: „Procedura salvată și view-ul sunt același obiect, doar că unul se apelează.”
    Cheie: Procedura se apelează cu CALL și nu stă în FROM ca tabelă virtuală. View-ul se citește cu SELECT.
    Demonstrație: View_Staff_Interview.separateFromBaseTable()
27. Enunț: TRUNCATE v_dept_emp_latest_date ca să golești grupurile. Ce golești de fapt dacă scrii pe tabelă?
    Capcană: „TRUNCATE pe view golește doar rezultatul salvat și lasă dept_emp.”
    Cheie: View-ul nu ține grupurile. Ca să dispară rândurile trebuie atinsă dept_emp. DROP VIEW scoate doar obiectul.
    Demonstrație: View_Staff_Interview.separateFromBaseTable()
28. Enunț: Un view cu UNION ALL pe două tabele pare teoretic scriibil. Îl acceptă 9.7?
    Capcană: „UNION ALL este updatable dacă fiecare parte este updatable.”
    Cheie: UNION și UNION ALL fac view-ul non-updatable, chiar dacă teoretic ai putea scrie într-o parte.
    Demonstrație: View_Staff_Interview.failOnInsert()
29. Enunț: v_dept_emp_open și v_dept_emp_latest_date citesc aceeași dept_emp. Care poate schimba dept_no?
    Capcană: „Ambele, pentru că ambele sunt view-uri pe aceeași tabelă.”
    Cheie: Doar fereastra cu coloane simple. Agregatul cu GROUP BY nu este updatable.
    Demonstrație: View_Staff_Interview.chooseOpenView()
30. Enunț: PDF-ul spune că view-ul este o tabelă virtuală temporară, fără scriere. Ce păstrezi din manual?
    Capcană: „PDF-ul este regula: zero memorie, structură urmărită automat, niciun CRUD.”
    Cheie: Rândurile stau în dept_emp și o scriere unu-la-unu poate ajunge acolo. Structura nu este urmărită. TEMPTABLE folosește memorie pe durata propoziției. Obiectul nu este temporar.
    Demonstrație: View_Staff_Interview.separateFromBaseTable()

Eșec în producție. 10.
31. Enunț: Un job face INSERT în v_dept_emp_latest_date ca să „închidă” data 1986-06-26. Ce rămâne în dept_emp?
    Capcană: „Rândul nou, pentru că view-ul este poarta de scriere.”
    Cheie: Nimic nou. INSERT-ul este respins. Data se scrie doar printr-o propoziție pe care view-ul agregat nu o acceptă.
    Demonstrație: View_Staff_Interview.failOnInsert()
32. Enunț: UPDATE v_dept_emp_open SET to_date = '2025-06-05' WHERE emp_no = 10001. De ce nu se închide perioada?
    Capcană: „WHERE-ul din view nu privește scrierea, deci 2025-06-05 înlocuiește 9999-01-01.”
    Cheie: WITH CHECK OPTION refuză rândul care nu ar mai trece to_date = 9999-01-01. Un UPDATE direct pe dept_emp ar scrie data.
    Demonstrație: View_Staff_Interview.rejectCheckedDate()
33. Enunț: Apelantul pune LIMIT 5 peste view-ul care are LIMIT 10 și paginează după acest număr. Ce se rupe?
    Capcană: „Primește mereu cinci grupuri, deci pagina este stabilă.”
    Cheie: Manualul lasă nedefinit care LIMIT se aplică. Pagina nu are un număr pe care să te bazezi.
    Demonstrație: View_Staff_Interview.undefinedLimit()
34. Enunț: CREATE OR REPLACE schimbă max_date în last_from. Clienții vechi ce primesc?
    Capcană: „OR REPLACE păstrează numele coloanelor și doar întinde SELECT-ul.”
    Cheie: Definiția nouă înlocuiește lista. Cine citea max_date nu mai are coloana. OR REPLACE cere privilegiul DROP.
    Demonstrație: View_Staff_Interview.recognizeStoredQuery()
35. Enunț: Contul care a creat view-ul are SELECT pe dept_emp. Un invoker are doar SELECT pe view, iar view-ul este SQL SECURITY DEFINER. Cine este verificat pe dept_emp?
    Capcană: „Invokerul trebuie să aibă SELECT pe dept_emp, altfel view-ul nu poate fi citit.”
    Cheie: La DEFINER, privilegiile pe obiectele din view sunt ale definitorului. Invokerul are nevoie de privilegiu pe view. La INVOKER, verificarea pe dept_emp folosește contul care apelează.
    Demonstrație: View_Staff_Interview.chooseOpenView()
36. Enunț: Cineva cere ALGORITHM = MERGE ca să poată face UPDATE pe v_dept_emp_latest_date. Ce avertisment urmează?
    Capcană: „MERGE câștigă și IS_UPDATABLE devine YES.”
    Cheie: View-ul nu poate fi îmbinat. Algoritmul rămâne UNDEFINED, cu avertisment, iar scrierea tot este respinsă.
    Demonstrație: View_Staff_Interview.failOnInsert()
37. Enunț: dept_emp este ștearsă după CREATE VIEW. Prima citire din view ce face?
    Capcană: „View-ul ține datele, deci SELECT-ul încă întoarce grupurile.”
    Cheie: Citirea eșuează. Tabela referită trebuie să existe. CHECK TABLE vede definiția stricată.
    Demonstrație: View_Staff_Interview.effectOnBaseRows()
38. Enunț: View-ul updatable nu cuprinde o coloană NOT NULL fără default din dept_emp. INSERT-ul prin view ce face?
    Capcană: „Este updatable, deci este și insertable.”
    Cheie: Updatable nu este insertable. Insertable cere toate coloanele fără default și doar referințe simple, nu expresii.
    Demonstrație: View_Staff_Interview.chooseOpenView()
39. Enunț: SET-ul dintr-un join view numește coloane din ambele tabele. UPDATE-ul trece?
    Capcană: „Dacă join-ul este INNER, ambele părți se scriu într-o propoziție.”
    Cheie: La un view cu mai multe tabele, updatable doar dacă se îmbină și doar o singură tabelă este în SET. DELETE pe un join view nu este permis.
    Demonstrație: View_Staff_Interview.failOnInsert()
40. Enunț: sql_mode se schimbă în ANSI_QUOTES între CREATE VIEW și SELECT. Poate rezultatul unui view de literal să difere?
    Capcană: „Definiția înghețată înseamnă că și rezultatul literalului este înghețat.”
    Cheie: Textul este înghețat, dar mediul de procesare nu. Manualul arată că ghilimelele din view schimbă rezultatul când ANSI_QUOTES se schimbă.
    Demonstrație: View_Staff_Interview.effectOnBaseRows()

Compromis, inclusiv când rămâi în aplicație. 10.
41. Enunț: Agregatul pe emp_no este folosit o singură dată, în mijlocul unei tranzacții care nu are voie să se închidă. Ce nu alegi?
    Capcană: „CREATE VIEW, pentru că este mai curat și nu atinge tranzacția.”
    Cheie: CREATE VIEW face commit implicit. Lași SELECT-ul ca tabelă derivată în propoziția curentă.
    Demonstrație: View_Staff_Interview.recognizeStoredQuery()
42. Enunț: Zece rapoarte citesc aceleași grupuri în fiecare oră și pot accepta un commit. Ce salvezi?
    Capcană: „O tabelă agregat, pentru că view-ul ocupă la fel de mult și nu se actualizează.”
    Cheie: CREATE VIEW salvează SELECT-ul și recalculează la citire. Nu copiezi dept_emp. Commit-ul implicit este prețul obiectului din schemă.
    Demonstrație: View_Staff_Interview.recognizeStoredQuery()
43. Enunț: Grupurile trebuie să rămână și după ce dept_emp se schimbă, până la un job de reîmprospătare. Ce nu este view-ul 9.7?
    Capcană: „MATERIALIZED pe CREATE VIEW ține copia pe acest server.”
    Cheie: View-ul recalculează. Copia durabilă este o tabelă pe care o scrii tu. MATERIALIZED din sintaxă este HeatWave, nu depozitul acestui view.
    Demonstrație: View_Staff_Interview.separateFromBaseTable()
44. Enunț: Ecranul trebuie să schimbe dept_no și să nu poată închide to_date. Ce view alegi?
    Capcană: „v_dept_emp_latest_date, pentru că deja limitează rândurile.”
    Cheie: v_dept_emp_open, cu coloane simple, WHERE to_date = 9999-01-01 și WITH CHECK OPTION. Agregatul nu primește UPDATE.
    Demonstrație: View_Staff_Interview.chooseOpenView()
45. Enunț: Regula „data de închidere nu poate fi în viitorul unui alt sistem” nu este în dept_emp. Unde o pui?
    Capcană: „În WITH CHECK OPTION, pentru că orice regulă de scriere este a view-ului.”
    Cheie: CHECK OPTION vede doar WHERE-ul view-ului. Regula din alt sistem rămâne în aplicație. View-ul nu o poate citi.
    Demonstrație: View_Staff_Interview.chooseOpenView()
46. Enunț: Vrei ca invokerul să nu aibă SELECT pe dept_emp, dar să citească grupurile. Ce caracteristică lași?
    Capcană: „SQL SECURITY INVOKER, ca să fie mai sigur.”
    Cheie: DEFINER, implicit, verifică dept_emp cu contul care a creat view-ul. INVOKER ar cere privilegiul pe dept_emp la apel.
    Demonstrație: View_Staff_Interview.chooseOpenView()
47. Enunț: Apelantul are nevoie de LIMIT propriu, stabil. Unde nu pui LIMIT?
    Capcană: „În view, ca să protejezi tabela, iar apelantul mai pune un LIMIT.”
    Cheie: LIMIT rămâne în propoziția apelantului. Două LIMIT-uri lasă efectul nedefinit.
    Demonstrație: View_Staff_Interview.effectOnBaseRows()
48. Enunț: Migrarea trebuie să creeze view-ul dacă lipsește și să nu îl strice dacă există. OR REPLACE sau IF NOT EXISTS?
    Capcană: „OR REPLACE, pentru că IF NOT EXISTS este doar un avertisment inutil.”
    Cheie: IF NOT EXISTS nu schimbă definiția existentă. OR REPLACE o înlocuiește și cere DROP. Nu le pui împreună.
    Demonstrație: View_Staff_Interview.recognizeStoredQuery()
49. Enunț: Planul de citire al grupurilor este lent. Indexul nou unde se creează?
    Capcană: „Pe view, ca obiect separat de dept_emp.”
    Cheie: Pe coloanele din dept_emp pe care le-ar folosi SELECT-ul dacă s-ar îmbină. Acest view nu se îmbină; indexul nu face INSERT-ul legal.
    Demonstrație: View_Staff_Interview.separateFromBaseTable()
50. Enunț: Aplicația ține deja lista de zece emp_no în memorie și nu vrea obiect în schemă. Mai creezi view-ul?
    Capcană: „Da, altfel serverul nu poate calcula MAX(from_date).”
    Cheie: Nu. SELECT-ul poate sta în aplicație sau ca tabelă derivată. View-ul este pentru interogarea salvată în schemă, cu prețul commit-ului implicit.
    Demonstrație: View_Staff_Interview.recognizeStoredQuery()
*/

/*
🟢 Începător
    Ce este            banca care recunoaște interogarea salvată pe dept_emp
    De ce e nevoie     fără metode, întrebările nu au propoziția pe care o arată
🟡 Producție
    Cum se scrie       CREATE OR REPLACE VIEW v_dept_emp_latest_date AS SELECT emp_no, MAX(from_date) AS max_date, MIN(from_date) AS min_date FROM dept_emp GROUP BY emp_no LIMIT 10
🔴 Staff
    Unde               metodele întorc șirul din DB_VIEW și nu au main
Pași                   1. recunoașterea
                       2. rândurile
                       3. deosebirea de tabelă
                       4. eșecul
                       5. fereastra updatable
*/
// Începător: clasa ține propozițiile pe care le cer cele 50 de întrebări.
// Producție: metodele întorc CREATE VIEW, SELECT, DROP VIEW, INSERT și fereastra deschisă. Dacă clasa dispare, banca nu mai are demonstrație.
// Expert: nu există main care să arate cheia. Nu trimite șirul către 3306.
public final class View_Staff_Interview
// Începător: de aici pleacă propozițiile, nu răspunsul citit cu voce tare.
// Producție: fără acoladă, cele cinci metode nu au clasă.
// Expert: acolada nu face commit implicit.
{

    /*
    🟢 Începător
        Ce este            propoziția care salvează agregatul pe emp_no
        De ce e nevoie     fără ea, întrebările de recunoaștere nu au textul din schemă
    🟡 Producție
        Cum se scrie       CREATE OR REPLACE VIEW v_dept_emp_latest_date AS SELECT emp_no, MAX(from_date) AS max_date, MIN(from_date) AS min_date FROM dept_emp GROUP BY emp_no LIMIT 10
    🔴 Staff
        Unde               CREATE VIEW face commit implicit și nu copiază dept_emp
    Pași                   1. întoarce createLatestDateView
    */
    // Începător: metoda dă interogarea salvată.
    // Producție: întoarce CREATE OR REPLACE VIEW. Dacă dispare, întrebările 1, 5, 6, 7, 14, 23, 25, 34, 36, 41, 42, 48 și 50 nu mai au textul.
    // Expert: nu este CREATE TEMPORARY VIEW și nu este MATERIALIZED pe acest server.
    public static String recognizeStoredQuery()
    // Începător: înăuntru pleacă obiectul din schemă, nu rândul.
    // Producție: fără acoladă, CREATE VIEW nu iese din bancă.
    // Expert: acolada nu combină OR REPLACE cu IF NOT EXISTS.
    {
        // Începător: înapoi pleacă propoziția pe care serverul o salvează.
        // Producție: return DB_VIEW.createLatestDateView() predă CREATE OR REPLACE VIEW. Fără linie, recunoașterea nu are SQL.
        // Expert: GROUP BY și LIMIT opresc MERGE. Privilegiul DROP este cerut de OR REPLACE, nu de o copie de rânduri.
        return DB_VIEW.createLatestDateView();
    // Începător: metoda de recunoaștere se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu înlocuiește o definiție deja prezentă pe server.
    }

    /*
    🟢 Începător
        Ce este            citirea care aduce grupurile din dept_emp
        De ce e nevoie     fără ea, nu se vede momentul în care data nouă apare
    🟡 Producție
        Cum se scrie       SELECT * FROM v_dept_emp_latest_date
    🔴 Staff
        Unde               rezultatul vine la sfârșitul SELECT-ului, pe coloanele înghețate
    Pași                   1. întoarce readLatestDates
    */
    // Începător: metoda dă citirea grupurilor.
    // Producție: întoarce SELECT * FROM v_dept_emp_latest_date. Dacă dispare, întrebările despre rând și despre LIMIT nu mai au citirea.
    // Expert: nu este SELECT-ul cu LIMIT 5. Acela stă în outerLimitOnLimitedView, iar efectul lui este nedefinit.
    public static String effectOnBaseRows()
    // Începător: înăuntru pleacă citirea, nu scrierea.
    // Producție: fără acoladă, SELECT-ul nu iese din bancă.
    // Expert: acolada nu eliberează lacătele de TEMPTABLE.
    {
        // Începător: înapoi pleacă cererea care recalculează grupurile.
        // Producție: return DB_VIEW.readLatestDates() predă SELECT * FROM v_dept_emp_latest_date. Fără linie, efectul pe rânduri nu are propoziție.
        // Expert: O coloană nouă în dept_emp nu intră. Lacătele de la TEMPTABLE nu țin cât există view-ul în schemă.
        return DB_VIEW.readLatestDates();
    // Începător: metoda efectului se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu citește planul. EXPLAIN rămâne NERULAT AICI.
    }

    /*
    🟢 Începător
        Ce este            scoaterea view-ului, care lasă angajații în tabelă
        De ce e nevoie     fără ea, view-ul și dept_emp par că dispar împreună
    🟡 Producție
        Cum se scrie       DROP VIEW v_dept_emp_latest_date
    🔴 Staff
        Unde               obiectul dispare la sfârșitul propoziției; rândurile rămân
    Pași                   1. întoarce dropLatestDateView
    */
    // Începător: metoda dă deosebirea dintre obiect și rând.
    // Producție: întoarce DROP VIEW. Dacă dispare, întrebările despre tabelă nu mai au propoziția de ieșire.
    // Expert: nu este DROP TABLE dept_emp și nu este TRUNCATE pe grupuri salvate.
    public static String separateFromBaseTable()
    // Începător: înăuntru pleacă scoaterea numelui.
    // Producție: fără acoladă, DROP VIEW nu iese din bancă.
    // Expert: acolada nu eliberează numele dept_emp pentru un view cu același nume.
    {
        // Începător: înapoi pleacă propoziția după care 10001 rămâne în dept_emp.
        // Producție: return DB_VIEW.dropLatestDateView() predă DROP VIEW v_dept_emp_latest_date. Fără linie, vecinul tabelă nu are demonstrație.
        // Expert: Commit implicit înainte și după. Indexul rămâne pe dept_emp, pentru că view-ul nu l-a ținut.
        return DB_VIEW.dropLatestDateView();
    // Începător: metoda deosebirii se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu șterge shift_log din transfer; acela nu este în acest șir.
    }

    /*
    🟢 Începător
        Ce este            scrierea care nu ajunge în dept_emp
        De ce e nevoie     fără ea, orice view pare updatable
    🟡 Producție
        Cum se scrie       INSERT INTO v_dept_emp_latest_date (emp_no, max_date, min_date) VALUES (10001, '1986-06-26', '1986-06-26')
    🔴 Staff
        Unde               serverul respinge propoziția; IS_UPDATABLE pentru acest view este NO
    Pași                   1. întoarce insertIntoLatestDateView
    */
    // Începător: metoda dă eșecul de scriere în view-ul agregat.
    // Producție: întoarce INSERT-ul. Dacă dispare, întrebările de eșec nu mai au textul.
    // Expert: nu este UPDATE-ul cu to_date = 2025-06-05. Acela eșuează prin CHECK OPTION, pe celălalt view.
    public static String failOnInsert()
    // Începător: înăuntru pleacă grupul care nu se scrie.
    // Producție: fără acoladă, INSERT-ul nu iese din bancă.
    // Expert: acolada nu schimbă updatable_views_with_limit.
    {
        // Începător: înapoi pleacă încercarea care nu pune 1986-06-26 în dept_emp.
        // Producție: return DB_VIEW.insertIntoLatestDateView() predă INSERT-ul. Fără linie, eșecul nu are propoziție.
        // Expert: max_date este expresie. ALGORITHM = TEMPTABLE ar respinge scrierea și fără agregat. Șirul nu a fost trimis.
        return DB_VIEW.insertIntoLatestDateView();
    // Începător: metoda eșecului se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu este eroarea 1050.
    }

    /*
    🟢 Începător
        Ce este            fereastra prin care departamentul se schimbă în dept_emp și data de închidere nu
        De ce e nevoie     fără ea, rămâne ideea că niciun view nu primește scriere
    🟡 Producție
        Cum se scrie       CREATE OR REPLACE VIEW v_dept_emp_open AS SELECT emp_no, dept_no, from_date, to_date FROM dept_emp WHERE to_date = '9999-01-01' WITH CHECK OPTION
    🔴 Staff
        Unde               relația unu-la-unu poate fi updatable; CHECK OPTION păzește to_date
    Pași                   1. întoarce createOpenAssignmentView
    */
    // Începător: metoda dă view-ul în care scrierea poate ajunge în tabelă.
    // Producție: întoarce CREATE VIEW cu WITH CHECK OPTION. Dacă dispare, compromisul față de agregat nu are propoziție.
    // Expert: nu este trigger și nu este SQL SECURITY INVOKER. Definitorul rămâne cel implicit.
    public static String chooseOpenView()
    // Începător: înăuntru pleacă fereastra rândurilor deschise.
    // Producție: fără acoladă, CREATE VIEW cu CHECK OPTION nu iese din bancă.
    // Expert: acolada nu face view-ul insertable dacă lipsește o coloană fără default.
    {
        // Începător: înapoi pleacă propoziția prin care un departament poate fi schimbat fără să închidă perioada.
        // Producție: return DB_VIEW.createOpenAssignmentView() predă CREATE OR REPLACE VIEW v_dept_emp_open. Fără linie, alegerea nu are SQL.
        // Expert: UPDATE-ul pe dept_no trece. UPDATE-ul pe to_date către 2025-06-05 este respins. Ambele stau în DB_VIEW, nu în această întoarcere.
        return DB_VIEW.createOpenAssignmentView();
    // Începător: metoda alegerii se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu acordă privilegiul pe dept_emp invokerului.
    }

    /*
    🟢 Începător
        Ce este            semnul pus la creare, înainte de orice INSERT
        De ce e nevoie     fără el, updatable pare decis abia la scriere
    🟡 Producție
        Cum se scrie       SELECT IS_UPDATABLE FROM information_schema.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'v_dept_emp_latest_date'
    🔴 Staff
        Unde               pentru view-ul cu GROUP BY semnul este NO
    Pași                   1. întoarce viewUpdatableFlag
    */
    // Începător: metoda dă citirea semnului de scriere.
    // Producție: întoarce SELECT IS_UPDATABLE. Dacă dispare, întrebarea 4 nu mai are textul.
    // Expert: steagul nu spune că v_dept_emp_open este insertable.
    public static String updatableFlag()
    // Începător: înăuntru se citește semnul, nu se scrie un grup.
    // Producție: fără acoladă, SELECT-ul pe VIEWS nu iese din bancă.
    // Expert: acolada nu schimbă steagul fixat la CREATE VIEW.
    {
        // Începător: înapoi pleacă întrebarea al cărei răspuns, pentru agregat, este că nu se poate scrie.
        // Producție: return DB_VIEW.viewUpdatableFlag() predă citirea lui IS_UPDATABLE. Fără linie, întrebarea 4 nu are propoziție.
        // Expert: Steagul se fixează la creare, nu la primul INSERT. Șirul nu a fost citit în tur.
        return DB_VIEW.viewUpdatableFlag();
    // Începător: metoda semnului se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu întoarce YES sau NO de pe server.
    }

    /*
    🟢 Începător
        Ce este            a doua limită pusă peste view-ul care are deja zece grupuri
        De ce e nevoie     fără ea, pagina pare stabilă la cinci rânduri
    🟡 Producție
        Cum se scrie       SELECT * FROM v_dept_emp_latest_date LIMIT 5
    🔴 Staff
        Unde               manualul lasă nedefinit care LIMIT se aplică
    Pași                   1. întoarce outerLimitOnLimitedView
    */
    // Începător: metoda dă citirea cu două limite.
    // Producție: întoarce SELECT-ul cu LIMIT 5. Dacă dispare, întrebările 10 și 33 nu mai au textul.
    // Expert: nu este ORDER BY. Acela chiar este înlocuit de clauza exterioară.
    public static String undefinedLimit()
    // Începător: înăuntru nu știi dacă vin cinci grupuri sau zece.
    // Producție: fără acoladă, LIMIT 5 nu iese din bancă.
    // Expert: acolada nu alege care limită câștigă.
    {
        // Începător: înapoi pleacă citirea nesigură.
        // Producție: return DB_VIEW.outerLimitOnLimitedView() predă SELECT * FROM v_dept_emp_latest_date LIMIT 5. Fără linie, efectul nedefinit nu are propoziție.
        // Expert: Limita stabilă stă doar în propoziția apelantului, nu și în view. Șirul nu a fost trimis.
        return DB_VIEW.outerLimitOnLimitedView();
    // Începător: metoda limitei se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu numără grupurile.
    }

    /*
    🟢 Începător
        Ce este            schimbarea de departament care ajunge în dept_emp
        De ce e nevoie     fără ea, CREATE VIEW pare că și scrie rândul
    🟡 Producție
        Cum se scrie       UPDATE v_dept_emp_open SET dept_no = 'd005' WHERE emp_no = 10001
    🔴 Staff
        Unde               UPDATE-ul nu face commit implicit; to_date rămâne 9999-01-01
    Pași                   1. întoarce updateDepartmentThroughOpenView
    */
    // Începător: metoda dă scrierea care trece prin fereastră.
    // Producție: întoarce UPDATE-ul pe dept_no. Dacă dispare, întrebările 15 și 20 nu mai au textul.
    // Expert: nu închide tranzacția. Nu este INSERT-ul în view-ul agregat.
    public static String writeDepartment()
    // Începător: înăuntru pleacă mutarea în d005.
    // Producție: fără acoladă, UPDATE-ul nu iese din bancă.
    // Expert: acolada nu pune lacăt după sfârșitul propoziției.
    {
        // Începător: înapoi pleacă schimbarea care se vede în dept_emp.
        // Producție: return DB_VIEW.updateDepartmentThroughOpenView() predă UPDATE-ul. Fără linie, scrierea updatable nu are propoziție.
        // Expert: CHECK OPTION lasă scrierea pentru că to_date nu se schimbă. Șirul nu a fost trimis.
        return DB_VIEW.updateDepartmentThroughOpenView();
    // Începător: metoda scrierii se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu face COMMIT.
    }

    /*
    🟢 Începător
        Ce este            închiderea perioadei pe care fereastra o refuză
        De ce e nevoie     fără ea, WHERE-ul pare doar un filtru de citire
    🟡 Producție
        Cum se scrie       UPDATE v_dept_emp_open SET to_date = '2025-06-05' WHERE emp_no = 10001
    🔴 Staff
        Unde               WITH CHECK OPTION refuză rândul care ar ieși din to_date = 9999-01-01
    Pași                   1. întoarce rejectClosingDateThroughCheckOption
    */
    // Începător: metoda dă scrierea oprită de fereastră.
    // Producție: întoarce UPDATE-ul pe to_date. Dacă dispare, întrebarea 32 nu mai are textul.
    // Expert: nu este refuzul de updatable al agregatului. Un UPDATE direct pe dept_emp ar scrie data.
    public static String rejectCheckedDate()
    // Începător: înăuntru data 2025-06-05 nu are voie să iasă din fereastră.
    // Producție: fără acoladă, UPDATE-ul respins nu iese din bancă.
    // Expert: acolada nu schimbă CASCADED în LOCAL.
    {
        // Începător: înapoi pleacă închiderea care nu ajunge în dept_emp prin acest view.
        // Producție: return DB_VIEW.rejectClosingDateThroughCheckOption() predă UPDATE-ul cu 2025-06-05. Fără linie, CHECK OPTION nu are propoziție.
        // Expert: Rândul ar dispărea din view. De aceea este respins. Șirul nu a fost trimis.
        return DB_VIEW.rejectClosingDateThroughCheckOption();
    // Începător: metoda refuzului de fereastră se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu scrie to_date în dept_emp.
    }
// Începător: banca se termină aici, fără un main care să arate cheile.
// Producție: fără închidere, cele cinci metode nu compilează.
// Expert: închiderea nu reduce cele 50 de întrebări și nu predă un alt folder din catalog.
}
