/*
TOPIC:
    Cincizeci de întrebări despre programul care pornește singur la rândul scris în salary sau în dept_manager.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI. EXPLAIN nu a fost citit.
INTENT:
    Recunoașterea din CREATE TRIGGER, din TRUNCATE sau din corpul care întoarce rânduri, momentul în care NEW se mai poate schimba, deosebirea de CHECK și de CALL, eșecul pe aceeași tabelă și alegerea față de aplicație.
MECHANISM:
    BEFORE INSERT face SET NEW.salary = 0 înainte de scriere și rândul rămâne. AFTER cu același SET nu schimbă rândul. CHECK refuză rândul. Eșecul trigger-ului face să cadă întreaga propoziție.
LOOKALIKES:
    CHECK refuză. CALL cere propoziția. OLD lipsește la INSERT. TRUNCATE nu este DELETE. Cascada cheii străine, implicit, nu pornește trigger-ul.
BOOK THEORY:
    Manualul 9.7, 27.4.1, 15.1.27 și 27.10, deschise în tur. PDF-ul din folder, pe paginile deschise, nu conține CREATE TRIGGER.
BOOK EXAMPLE:
    Probele din Triggers, BEFORE_ și AFTER_, nu o metodă din această clasă.
REPO DEMO:
    src/main/java/_15_TRIGGERS/Triggers.java
BREAK LAB:
    SET NEW într-un AFTER, OLD într-un INSERT, sau UPDATE pe tabela care a pornit trigger-ul.
TRADE-OFFS:
    CHECK când rândul trebuie să cadă. CALL când programul trebuie cerut. Aplicația când regula are nevoie de un sistem pe care propoziția nu îl vede.
ANTI-PATTERNS:
    SET NEW după scriere, TRUNCATE crezut DELETE, COMMIT în corp, UPDATE pe aceeași tabelă.
STAFF QUESTIONS:
    Întrebările 1–50 de mai jos. Cheia este scrisă. Nu există main care să o citească singur.
NOVEL TRANSFER:
    Mișcarea de raft pornește schimbarea cantității. Cantitatea negativă păstrată ca 0 este BEFORE. Refuzul este CHECK.
SPACED REVIEW:
    O zi: întrebările 11–20. Șapte zile: 31–40. Treizeci de zile: 41–50.

Definiția unui trigger (trigger):
       Trigger-ul este programul legat de o tabelă permanentă, pornit de server la INSERT, UPDATE sau DELETE, o dată pentru fiecare rând.

Exemplu:
       Triggers_Terms.explainTrigger()

Definiția unei propoziții (statement):
       Propoziția care scrie rândul pornește trigger-ul. Dacă trigger-ul eșuează, întreaga propoziție eșuează.

Exemplu:
       Triggers_Terms.explainStatement()

Recunoaștere din SQL, EXPLAIN sau eroare:
    1. Enunț: În CREATE TRIGGER before_salary_insert BEFORE INSERT ON salary FOR EACH ROW vezi IF NEW.salary < 0 THEN SET NEW.salary = 0. Ce rămâne după INSERT-ul cu -50?
       Capcană: Rândul este respins, ca la CHECK (salary >= 0).
       Cheie: Rândul rămâne, cu 0. SET NEW s-a făcut înainte de scriere. CHECK nu ar fi lăsat rândul.
    2. Enunț: Ce recunoști în CREATE TRIGGER after_salary_insert AFTER INSERT ON salary FOR EACH ROW SET NEW.salary = 0?
       Capcană: Este același lucru cu BEFORE, doar că mesajul pleacă după INSERT.
       Cheie: SET NEW nu are efect, fiindcă rândul a fost deja scris. Dacă coloana a acceptat -50, -50 rămâne. Nu este o eroare care dă propoziția înapoi.
    3. Enunț: Clientul trimite TRUNCATE TABLE salary, iar pe salary există un trigger AFTER DELETE. Ce corp a rulat?
       Capcană: Corpul de DELETE, o dată pentru fiecare rând, fiindcă tabela a rămas goală.
       Cheie: Niciunul. 15.1.27: TRUNCATE TABLE și DROP TABLE nu activează trigger-ul de DELETE, pentru că nu folosesc DELETE. Nici ștergerea unei partiții nu îl activează.
    4. Enunț: Ce recunoști în INSERT INTO salary ... ON DUPLICATE KEY UPDATE salary = 0, cu trigger-e BEFORE INSERT, AFTER INSERT, BEFORE UPDATE și AFTER UPDATE pe salary?
       Capcană: Rulează doar trigger-ele de INSERT, fiindcă propoziția începe cu INSERT.
       Cheie: BEFORE INSERT pornește pentru fiecare rând. Apoi fie AFTER INSERT, fie BEFORE UPDATE și AFTER UPDATE, după cum cheia era sau nu deja acolo.
    5. Enunț: CREATE TRIGGER ... ON salary_view. salary_view este un view peste salary. Ce obiect primește programul?
       Capcană: View-ul, fiindcă de acolo citește aplicația.
       Cheie: Niciunul. 15.1.27: trigger-ul nu se asociază cu un view și nici cu o tabelă TEMPORARY. Stă pe tabela permanentă salary.
    6. Enunț: LOAD DATA încarcă rânduri în salary. BEFORE INSERT de pe salary pornește?
       Capcană: Nu, fiindcă textul propoziției nu este INSERT.
       Cheie: Da. 15.1.27: trigger-ul de INSERT pornește la INSERT, LOAD DATA și REPLACE, pentru că toate scriu rânduri. Evenimentul este operația pe tabelă, nu cuvântul din prima poziție.
    7. Enunț: Două trigger-e BEFORE UPDATE pe salaries, al doilea creat mai târziu, fără FOLLOWS și fără PRECEDES. Care pornește primul?
       Capcană: Cel al cărui nume este primul alfabetic.
       Cheie: Cel creat primul. Aceeași pereche eveniment și moment pornește în ordinea creării. PRECEDES îl pune pe cel nou înaintea celui numit. FOLLOWS îl pune după.
    8. Enunț: Corpul face SELECT salary FROM salaries WHERE emp_no = NEW.emp_no, fără INTO. Ce primește clientul?
       Capcană: Rândurile, fiindcă SELECT din trigger este un rezultat ca oricare altul.
       Cheie: Trigger-ul nu poate întoarce un set de rezultate. 27.10 oprește SELECT fără INTO, și la fel SHOW, EXPLAIN și CHECK TABLE. Calea permisă este SELECT ... INTO.
    9. Enunț: Corpul începe cu START TRANSACTION și se termină cu COMMIT. Ce tranzacție nouă se deschide?
       Capcană: Mărirea se închide singură, chiar dacă INSERT-ul de manager dă mai târziu eroare.
       Cheie: Niciuna. 27.4.1 și 27.10: START TRANSACTION, COMMIT și ROLLBACK nu sunt permise în trigger. ROLLBACK TO SAVEPOINT este permis, pentru că nu încheie tranzacția.
    10. Enunț: EXPLAIN în corpul trigger-ului, ca să vezi planul INSERT-ului care l-a pornit. Ce plan se întoarce?
        Capcană: Planul propoziției părinte, fiindcă trigger-ul rulează în ea.
        Cheie: Niciunul către client. EXPLAIN întoarce un set de rezultate, iar trigger-ul nu poate. În tur, EXPLAIN nici nu a fost citit: root@localhost a primit 1045.

Rânduri, index, lacăt și momentul în care se vede efectul:
    11. Enunț: BEFORE INSERT pune NEW.salary = 0. Când vezi 0 în rândul scris?
        Capcană: La COMMIT. Până atunci clientul vede -50.
        Cheie: La rândul scris de INSERT. BEFORE a schimbat valoarea înainte de scriere. Trigger-ul nu așteaptă COMMIT și nu poate să-l dea.
    12. Enunț: BEFORE eșuează la al doilea rând dintr-un INSERT cu mai multe rânduri, pe InnoDB. Ce rămâne din primul rând?
        Capcană: Primul rămâne, fiindcă trigger-ul a reușit pe el.
        Cheie: Operația pe rândul la care BEFORE a eșuat nu se face. Eroarea face să cadă întreaga propoziție. Pe tabela tranzacțională, propoziția se dă înapoi, deci nici primul rând nu rămâne.
    13. Enunț: Un BEFORE a eșuat. Mai pornește AFTER de pe același rând?
        Capcană: Da, ca să scrie eroarea într-o tabelă de audit.
        Cheie: Nu. 27.4.1: AFTER se execută doar dacă trigger-ele BEFORE și operația pe rând au reușit.
    14. Enunț: Aceeași eroare la al treilea rând, pe o tabelă netranzacțională. Ce rămâne din primele două?
        Capcană: Nimic, ca pe InnoDB.
        Cheie: Propoziția eșuează, dar darea înapoi nu se poate face. Schimbările dinaintea erorii rămân. 27.4.1 desparte tabela tranzacțională de cea netranzacțională.
    15. Enunț: În BEFORE INSERT, coloana emp_no este AUTO_INCREMENT. Ce valoare are NEW.emp_no?
        Capcană: Următorul număr din secvență, fiindcă serverul l-a ales înainte de trigger.
        Cheie: 0. 27.4.1: într-un BEFORE, NEW pe AUTO_INCREMENT este 0, nu numărul generat la scrierea rândului.
    16. Enunț: LOCK TABLES salary WRITE, iar trigger-ul de pe salary face INSERT în audit_salary. Ce tabele mai ia lacătul?
        Capcană: Doar salary, fiindcă asta a numit LOCK TABLES.
        Cheie: Și tabelele folosite în trigger. 15.1.27: dacă blochezi o tabelă care are trigger-e, tabelele din corp sunt și ele blocate.
    17. Enunț: ON DELETE CASCADE șterge rândurile copil. Trigger-ul AFTER DELETE de pe copil pornește, implicit?
        Capcană: Da, o dată pe fiecare copil, fiindcă rândul a fost șters.
        Cheie: Nu, implicit. 15.1.27: acțiunile în cascadă nu activează trigger-e. enable_cascade_triggers=ON are efect doar când innodb_native_foreign_keys=OFF, la CASCADE și SET NULL, și nu are efect când innodb_native_foreign_keys=ON. 27.10 spune că trigger-ele nu sunt activate de acțiunile cheii străine.
    18. Enunț: INSERT scrie trei rânduri în salary, iar trigger-ul este FOR EACH ROW. De câte ori pornește corpul?
        Capcană: O dată, la sfârșitul propoziției.
        Cheie: De trei ori. 27.4.1: corpul de după FOR EACH ROW se execută o dată pentru fiecare rând atins. Sintaxa din 15.1.27 nu are un trigger pe toată propoziția.
    19. Enunț: La CREATE, sql_mode este unul. La INSERT, sesiunea are altul. Care mod execută corpul?
        Capcană: Modul sesiunii care face INSERT, fiindcă atunci rulează programul.
        Cheie: Modul salvat la CREATE. 15.1.27 și 27.4.1: trigger-ul se execută cu sql_mode de la creare, indiferent de modul curent.
    20. Enunț: CURRENT_USER() în corpul trigger-ului ce cont întoarce?
        Capcană: Contul care a trimis INSERT-ul, ca să știi cine a schimbat salariul.
        Cheie: DEFINER, contul cu care se verifică privilegiile la activare. 15.1.27: nu este contul a cărui propoziție a pornit trigger-ul. SET NEW cere UPDATE pe tabela subiect pentru DEFINER.

Deosebire de mecanismul vecin:
    21. Enunț: De ce CHECK (salary >= 0) nu este before_salary_insert?
        Capcană: Ambele opresc -50, deci tabela arată la fel.
        Cheie: CHECK refuză rândul. BEFORE scrie 0 și rândul rămâne. Trigger-ul rulează cod. Constrângerea refuză rândul. Trigger-ul nu ține locul lui FOREIGN KEY.
    22. Enunț: De ce CALL raise_floor() nu este acest trigger?
        Capcană: Ambele sunt programe păstrate, deci INSERT cheamă procedura singur.
        Cheie: Procedura pornește când clientul trimite CALL. Trigger-ul este legat de tabelă și pornește la eveniment, fără CALL. Din trigger poți face CALL doar către o procedură care nu întoarce date clientului și nu folosește SQL dinamic.
    23. Enunț: De ce o funcție în lista de valori a INSERT-ului nu este trigger-ul?
        Capcană: Funcția pornește automat, deci este un trigger fără CREATE TRIGGER.
        Cheie: Funcția întoarce o valoare fiindcă propoziția o numește. Trigger-ul nu este numit de INSERT. 27.10: RETURN nu este permis în trigger. Ca să ieși din corp folosești LEAVE.
    24. Enunț: De ce BEFORE INSERT nu poate citi OLD.salary?
        Capcană: OLD este 0, valoarea implicită a coloanei.
        Cheie: La INSERT există doar NEW. La DELETE există doar OLD. La UPDATE există ambele. OLD este doar citit.
    25. Enunț: De ce un eveniment din Event Scheduler, la fiecare promovare de luni, nu este acest trigger?
        Capcană: Ambele pornesc fără ca cineva să scrie corpul în INSERT, deci sunt același automatism.
        Cheie: Evenimentul pornește la programare. Trigger-ul pornește pentru că un rând a fost inserat, actualizat sau șters pe tabela lui. PDF-ul leagă evenimentul de aceste trei propoziții DML.
    26. Enunț: De ce un view cu WITH CHECK OPTION nu ține trigger-ul de pe salary?
        Capcană: View-ul este tabela pe care o vede aplicația, deci programul stă pe el.
        Cheie: 15.1.27 interzice trigger-ul pe view. CHECK OPTION filtrează rândurile view-ului. Trigger-ul stă pe tabela permanentă.
    27. Enunț: De ce REPLACE INTO salary nu este doar un INSERT pentru trigger-e?
        Capcană: REPLACE începe ca înlocuire, deci pornește doar BEFORE INSERT.
        Cheie: 15.1.27: DELETE se activează și la REPLACE, pentru că REPLACE șterge rândul vechi. INSERT se activează tot la REPLACE, pentru că scrie rândul nou.
    28. Enunț: De ce ON DELETE CASCADE nu este trigger-ul AFTER DELETE?
        Capcană: Cascada este trigger-ul scris de server pentru ștergere.
        Cheie: Cascada este acțiunea cheii străine. Implicit nu activează trigger-e. Trigger-ul este corpul tău. Nu înlocuiește FOREIGN KEY.
    29. Enunț: De ce o coloană generată nu este BEFORE care calculează valoarea?
        Capcană: Ambele umplu coloana înainte să rămână rândul, deci sunt același mecanism.
        Cheie: Coloana generată este în definiția tabelei. 15.1.27: trigger-ul nu poate folosi NEW sau OLD pe o coloană generată. Corpul trigger-ului este alt obiect.
    30. Enunț: De ce un index pe numele trigger-ului nu stabilește ordinea a două BEFORE UPDATE?
        Capcană: Indexul ordonează numele, deci ordonează și pornirea.
        Cheie: Ordinea este ordinea creării, sau FOLLOWS, sau PRECEDES, față de alt trigger cu același eveniment și același moment. Indexul nu pornește corpul.

Eșec în producție:
    31. Enunț: AFTER INSERT pe salary face UPDATE salary SET salary = 0 WHERE emp_no = NEW.emp_no. Ce se întâmplă?
        Capcană: Salariul devine 0, fiindcă AFTER vede rândul scris și poate să-l actualizeze.
        Cheie: 27.10: trigger-ul nu poate modifica o tabelă deja folosită de propoziția care l-a pornit. Corpul de promovare este permis fiindcă INSERT-ul este pe dept_manager, iar UPDATE-ul este pe salaries.
    32. Enunț: BEFORE încearcă să preface textul 'abc' în 0 pe o coloană întreagă. Când intervine?
        Capcană: SET NEW.salary = 0 salvează rândul, fiindcă rulează înainte de scriere.
        Cheie: Nu ajunge să-l salveze. 15.1.27: verificarea de bază a valorii pe tipul coloanei este înainte de activarea trigger-ului. BEFORE nu preface o valoare nepotrivită tipului într-una validă.
    33. Enunț: Corpul scrie mărirea și apoi COMMIT. INSERT-ul de manager eșuează după aceea. Mărirea rămâne?
        Capcană: Da, pentru că COMMIT a închis-o înainte de eroare.
        Cheie: COMMIT nu este permis în trigger. Mărirea este în aceeași propoziție. Pe tabela tranzacțională, eșecul propoziției dă înapoi și ce a scris trigger-ul.
    34. Enunț: INSERT cu două rânduri, trigger-ul eșuează la al doilea, InnoDB. Primul salariu rămâne?
        Capcană: Da. Doar rândul greșit cade.
        Cheie: Nu. Întreaga propoziție eșuează și, pe tabela tranzacțională, se dă înapoi. Primul rând nu rămâne.
    35. Enunț: Sursa scrie un salariu printr-un trigger, iar replica are același trigger. Replicarea este pe rând. Trigger-ul de pe replica mai pornește?
        Capcană: Da, deci mărirea se aplică a doua oară.
        Cheie: Nu. 27.10: la replicarea pe rând, trigger-ele de pe replica nu pornesc pentru propozițiile venite de la sursă. La replicarea pe propoziție, pornesc. Schimbările de rând ale sursei sunt deja în evenimentele de rând.
    36. Enunț: before_salary_insert există pe salary. Îl creezi cu același nume pe salaries, în aceeași schemă. Ce cade?
        Capcană: Nimic. Numele este unic pe tabelă.
        Cheie: Numele trigger-ului este unic în schemă. 15.1.27 și 27.4.1. În altă schemă, același nume poate exista. IF NOT EXISTS evită eroarea doar dacă acel nume există deja.
    37. Enunț: După CREATE schimbi sql_mode în sesiune și te bazezi pe modul nou în corp. Ce mod vede corpul la următorul INSERT?
        Capcană: Modul nou, fiindcă sesiunea l-a schimbat.
        Cheie: Modul de la CREATE. Sesiunea care face INSERT nu îl înlocuiește.
    38. Enunț: DEFINER a pierdut UPDATE pe salary. Invocatorul INSERT-ului încă are UPDATE. SET NEW.salary = 0 mai este permis la activare?
        Capcană: Da, pentru că cel care scrie rândul are dreptul.
        Cheie: Nu. La activare, privilegiile se verifică pe DEFINER. SET NEW cere UPDATE pe tabela subiect pentru DEFINER. TRIGGER pe tabela subiect trebuie să existe tot la DEFINER.
    39. Enunț: Trigger-ul citește o tabelă al cărei metadate s-au schimbat după ce corpul a fost încărcat în cache. Ce definiție folosește?
        Capcană: Definiția nouă, la următorul INSERT.
        Cheie: 27.10: cache-ul trigger-ului nu detectează schimbarea metadatelor obiectelor de dedesubt. Poate lucra cu metadatele vechi.
    40. Enunț: SET enable_cascade_triggers=ON, iar innodb_native_foreign_keys=ON. Cascada pornește trigger-ul copilului?
        Capcană: Da, pentru că variabila este ON.
        Cheie: Nu. 15.1.27: setarea nu are efect când innodb_native_foreign_keys=ON. Implicit, cascada nu activează trigger-ul.

Alegere, inclusiv când rămâi în aplicație:
    41. Enunț: Salariul negativ nu are voie să lase un rând. Pui BEFORE cu SET NEW.salary = 0?
        Capcană: Da, fiindcă 0 este un salariu valid și rândul trebuie să existe.
        Cheie: Nu, dacă regula este că rândul nu rămâne. CHECK (salary >= 0) refuză rândul. BEFORE cu 0 păstrează altceva decât a trimis clientul. Trigger-ul este pentru rescriere, nu pentru refuz.
    42. Enunț: Mărirea se face doar când un om o cere, nu la fiecare rând din dept_manager. Unde stă programul?
        Capcană: În AFTER, fiindcă automat înseamnă că nu se uită.
        Cheie: Într-o procedură, pornită cu CALL. Trigger-ul nu poate fi sărit de INSERT-ul care este evenimentul lui. Dacă unele promovări nu măresc salariul, trigger-ul automat este unealta greșită.
    43. Enunț: Vrei id-ul AUTO_INCREMENT ca să scrii un rând copil în altă tabelă, în aceeași propoziție. BEFORE sau AFTER?
        Capcană: BEFORE, fiindcă pornește primul și NEW.id este deja numărul.
        Cheie: AFTER. În BEFORE, NEW pe AUTO_INCREMENT este 0. AFTER pornește după ce scrierea a reușit. Copilul este altă tabelă, deci nu este tabela deja folosită de propoziție.
    44. Enunț: Vrei un rând de audit cu valoarea care a rămas scrisă, nu cu încercarea eșuată. BEFORE sau AFTER?
        Capcană: BEFORE, ca să prinzi și încercările.
        Cheie: AFTER pentru imaginea scrisă: pornește doar după succes. 27.4.1: BEFORE pornește la încercare, chiar dacă încercarea nu reușește apoi. Auditul încercărilor este BEFORE. Niciunul nu ține locul binlog-ului.
    45. Enunț: Același corp trebuie să pornească din trei trigger-e. Îl copiezi de trei ori?
        Capcană: Da, ca un trigger să nu depindă de o procedură.
        Cheie: 27.4.1: poți defini o procedură și să o chemi cu CALL, dacă nu întoarce un set de rezultate și nu folosește SQL dinamic. Un corp, trei trigger-e.
    46. Enunț: Regula de salariu stă într-un serviciu din afara lui MySQL. O pui în trigger?
        Capcană: Da, ca baza să rămână locul adevărului și trigger-ul să cheme serviciul.
        Cheie: În aplicație. Trigger-ul nu deschide serviciul acela. Nu poate încheia singur tranzacția, iar propoziția ține lucrul pe rând până când corpul se întoarce.
    47. Enunț: Data de angajare din viitor trebuie să rămână ca azi, nu să cadă. Ajunge CHECK (hire_date <= data de azi)?
        Capcană: Da, și CHECK rescrie data.
        Cheie: Nu. CHECK refuză rândul. trig_hire_date face SET NEW.hire_date la data de azi, deci rândul rămâne. Rescrierea este trigger-ul. Refuzul este constrângerea.
    48. Enunț: Ștergi tabela salary ca să o creezi din nou, cu același nume. Trebuie să faci DROP TRIGGER înainte?
        Capcană: Da, altfel trigger-ul vechi pornește pe tabela nouă.
        Cheie: Nu ca să dispară trigger-ul vechi. 27.4.1: dacă ștergi tabela, trigger-ele ei se șterg odată cu ea. Tabela nouă nu le moștenește.
    49. Enunț: Două BEFORE INSERT trebuie să pornească într-o ordine care nu este ordinea creării. Ce scrii?
        Capcană: Un index pe numele trigger-ului.
        Cheie: PRECEDES sau FOLLOWS și numele celuilalt trigger, cu același eveniment și același moment.
    50. Enunț: O scanare scrie o mișcare. Cantitatea din raft, altă tabelă, trebuie să se schimbe în aceeași propoziție. O cantitate negativă trebuie păstrată ca 0, nu refuzată. Ce este fiecare?
        Capcană: Un singur CHECK face ambele, fiindcă ambele apără stocul.
        Cheie: AFTER INSERT pe mișcare schimbă raftul, altă tabelă. BEFORE pe coloana de cantitate face SET NEW la 0, iar rândul rămâne. CHECK refuză rândul în loc să scrie 0. O procedură ar aștepta CALL și ar pierde scanarea.

🟢 Începător
    Ce este            banca de interviu pentru programul care pornește la rând, cu cheia scrisă
    De ce e nevoie     fără cele cincizeci, SET NEW în AFTER și TRUNCATE rămân aceeași poveste
🟡 Producție
    Cum se scrie       cincizeci de enunțuri în comentariu, fără main
🔴 Staff
    Unde               cheia stă aici. Proba care ține SQL-ul stă în Triggers, BEFORE_ și AFTER_. Această clasă nu o trimite.
Pași
    1. Citește enunțul.
    2. Cheia este deja scrisă, pentru artefact.
    3. Nu porni un main de recall.
*/
// Începător: banca stă lângă lecția scurtă și nu scrie un salariu.
// Producție: package _15_TRIGGERS.fast. Nu are main de recall.
// Expert: Cele cincizeci sunt despre trigger-ul de pe salary și dept_manager. Nu se lipesc pe CHECK ca și cum ar fi același folder.
package _15_TRIGGERS.fast;

// Începător: clasa ține întrebările și cheile în comentariul de deasupra.
// Producție: public final class Triggers_Staff_Interview. Dacă primește main, nu mai este banca fără recall.
// Expert: Nu execută INSERT-ul cu -50. Efectul pe server rămâne NERULAT în tur.
public final class Triggers_Staff_Interview {
    // Începător: nu există o propoziție în corp.
    // Producție: acolada închide Triggers_Staff_Interview. Sub ea nu se adaugă un main.
    // Expert: Cele cincizeci rămân în comentariu. Golul nu șterge o cheie.
}
