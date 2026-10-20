package _8_Constraints;



/*                                          Constraints


    ○ are specific rules or limits that we define in our tables.

                  * The role of constraints is

                                - to outline the existing relationships between different tables in our database.
                                  HISTORICAL: propoziția de deasupra nu este regula. Manualul 9.7, 1.7.3, deschis în tur: serverul încearcă să semnaleze ce poate detecta la parsare sau la execuție. Doar FOREIGN KEY leagă rândul copil de un părinte. PRIMARY KEY, UNIQUE și NOT NULL privesc valoarea din rândul acesta. Pe InnoDB, propoziția care încalcă regula se dă înapoi la sfârșitul ei.


 */

/*
TOPIC:
    Constrângerea este regula pe care serverul o verifică când se scrie un rând. Alegi copilul după faptul pe care trebuie să-l refuze sau să-l umple.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    O înscriere la același curs de două ori, sau la un curs care nu există, nu are voie să rămână în course_registration. După propoziția respinsă, rândul nu este în tabelă.
MECHANISM:
    Serverul execută șirul din registrationRulesTheServerEnforces. PRIMARY KEY refuză perechea repetată. FOREIGN KEY refuză course_id care nu este în course. Pe InnoDB refuzul se vede la sfârșitul propoziției, nu la COMMIT. Verificarea FOREIGN KEY nu este amânată.
LOOKALIKES:
    Un index secundar pe grade grăbește căutarea și nu refuză nota repetată. Un trigger rulează cod și nu ține locul lui FOREIGN KEY. NOT NULL refuză lipsa. DEFAULT umple coloana doar când INSERT-ul o omite.
BOOK THEORY:
    Manualul 9.7, 1.7.3 și 1.7.3.1, deschise în tur: la încălcarea lui PRIMARY KEY, UNIQUE sau FOREIGN KEY apare eroare; pe InnoDB propoziția se dă înapoi; pe un motor netranzacțional serverul se oprește la rândul greșit și lasă rândurile dinaintea lui. INSERT IGNORE sare peste încălcarea de cheie primară sau unică și continuă. sql_mode implicit include STRICT_TRANS_TABLES: pe tabela tranzacțională valoarea invalidă sau lipsă oprește propoziția. 15.1.25.5 și 1.7.2.3: InnoDB verifică FOREIGN KEY rând cu rând, imediat, și pune lacăt partajat pe înregistrările pe care le examinează; foreign_key_checks=0 sare verificarea la DML, iar repornirea variabilei nu scanează rândurile deja scrise. 15.1.25.6: CHECK eșuează când expresia este FALSE; NOT ENFORCED o creează fără să o aplice. 5.6.9 și 17.6.1.6: AUTO_INCREMENT generează numărul; pe InnoDB coloana trebuie să fie prima sau singura dintr-un index. hibernate-primary-keys.pdf, deschis în tur: cheia primară identifică rândul, este unică și nu poate fi NULL. Primary_Key.pdf, FOREIGN_KEY_Constraint.pdf și MySQL+Indexes.pdf, deschise în tur, încep cu titlul de slide. NOT_NULL_Constraint.pdf, deschis în tur: la NULL VALUES, unique key este yes și primary key este no. PDF-urile deschise nu spun că InnoDB dă propoziția înapoi.
BOOK EXAMPLE:
    registrationRulesTheServerEnforces
REPO DEMO:
    src/main/java/_8_Constraints/Constraints.java
BREAK LAB:
    insertIgnoreSkipsTheDuplicateGrade ține INSERT IGNORE cu aceeași pereche de două ori. A doua notă nu rămâne, iar prima da. În tur șirul nu a fost trimis: autentificarea a fost refuzată.
TRADE-OFFS:
    INDEX secundar când duplicatele sunt legale și vrei doar căutarea. UNIQUE când tabela are deja o cheie primară și NULL trebuie acceptat. Aplicația când regula are nevoie de un sistem pe care propoziția nu îl vede.
ANTI-PATTERNS:
    INSERT IGNORE ca să „treacă” înscrierea dublă, sau un trigger în loc de FOREIGN KEY.
STAFF QUESTIONS:
    Întrebările 1–50 din Constraints_Staff_Interview. Cheia este scrisă acolo. Nu există main care să o citească.
NOVEL TRANSFER:
    Un depozit: un colet nu se înscrie de două ori pe același raft, iar raftul trebuie să existe. Perechea este cheia. Raftul lipsă este FOREIGN KEY. Căutarea după culoare, dacă două colete pot avea aceeași culoare, este un index, nu o constrângere.
SPACED REVIEW:
    O zi: 21 octombrie 2026, rândul respins nu rămâne. Șapte zile: 27 octombrie 2026, INSERT IGNORE nu este un refuz vizibil. Treizeci de zile: 19 noiembrie 2026, indexul secundar nu ține locul cheii.

Definiția unei constrângeri (constraint):
       Constrângerea este regula verificată de server la propoziția care scrie rândul. Rândul care o încalcă nu rămâne.

Exemplu:
       Constraints_Terms.explainConstraint()
       Înscrierea la cursul 404 nu rămâne dacă course_id este FOREIGN KEY și 404 lipsește din course.

Definiția unei tabele (table):
       Tabela este locul din schemă unde regula este declarată. INSERT-ul de după este momentul în care rândul este judecat.

Exemplu:
       Constraints_Terms.explainTable()
       course ține părintele. course_registration ține copilul.

Definiția unei coloane (column):
       Coloana este valoarea pe care regula o vede. NOT NULL refuză lipsa pe coloana aceea. DEFAULT o umple doar când INSERT-ul nu o dă.

Exemplu:
       Constraints_Terms.explainColumn()
       grade poate lipsi. student_id nu poate.

Definiția unui index (index):
       Indexul este structura prin care cheia găsește duplicatul. Un index secundar fără UNIQUE nu refuză valoarea repetată.

Exemplu:
       Constraints_Terms.explainIndex()
       Două note A rămân dacă pe grade stă doar un index.

Definiția unei propoziții (statement):
       Efectul se vede la sfârșitul propoziției. Pe InnoDB, încălcarea se dă înapoi cu propoziția. COMMIT-ul nu salvează rândul deja respins.

Exemplu:
       Constraints_Terms.explainStatement()
       INSERT-ul cu 404 nu lasă rândul, iar tranzacția nu așteaptă commit-ul ca să afle refuzul.

1. Enunț: Ce problemă rezolvă alegerea constrângerii pe course_registration?
   Capcană: Aplicația verifică înainte de INSERT, deci a doua înscriere și cursul lipsă nu pot ajunge în tabelă.
   Cheie: Doi clienți pot trece de verificarea din aplicație. registrationRulesTheServerEnforces pune PRIMARY KEY pe (student_id, course_id) și FOREIGN KEY pe course_id. Serverul refuză perechea repetată și cursul care nu există. Rândul nu rămâne.
   Demonstrație: registrationRulesTheServerEnforces
2. Enunț: Ce se întâmplă cu rândul și cu tranzacția când INSERT-ul numește cursul 404, iar 404 nu este în course?
   Capcană: Rândul rămâne până la COMMIT, iar FOREIGN KEY se verifică abia atunci, ca în standardul care amână verificarea.
   Cheie: childCourseTheForeignKeyRefuses este respins la sfârșitul propoziției. Pe InnoDB propoziția se dă înapoi. Verificarea nu este amânată până la commit. InnoDB pune lacăt partajat pe înregistrările pe care le examinează, pe durata verificării.
   Demonstrație: childCourseTheForeignKeyRefuses
3. Enunț: De ce un index pe course_id, sau un trigger înainte de INSERT, nu este această alegere?
   Capcană: Indexul găsește cursul mai repede, deci cursul lipsă nu poate fi scris. Trigger-ul semnalează el, deci FOREIGN KEY este de prisos.
   Cheie: secondaryIndexKeepsBothGrades nu refuză o valoare. Un index pe course_id nu refuză 404. triggerInsteadOfForeignKey rulează codul tău. Catalogul îl desparte de constrângere: trigger-ul nu ține locul lui FOREIGN KEY. Refuzul părintelui lipsă este CONSTRAINT FOREIGN KEY.
   Demonstrație: childCourseTheForeignKeyRefuses
4. Enunț: Cum eșuează INSERT IGNORE când aceeași pereche vine de două ori, cu note diferite?
   Capcană: IGNORE înseamnă că ambele note rămân, iar serverul le împacă.
   Cheie: insertIgnoreSkipsTheDuplicateGrade sare peste a doua pereche și poate lăsa un avertisment. Prima notă rămâne. A doua nu. Nu este un ROLLBACK al primei înscrieri și nu este mesajul 1452 al unui părinte lipsă.
   Demonstrație: insertIgnoreSkipsTheDuplicateGrade
5. Enunț: Când lași un index secundar și când rămâi pe cheie sau în aplicație?
   Capcană: Orice coloană căutată trebuie să fie PRIMARY KEY, altfel căutarea este lentă și datele sunt greșite.
   Cheie: INDEX pe grade când două înscrieri pot avea aceeași notă și vrei doar căutarea. UNIQUE când există deja o cheie primară și NULL trebuie să încapă. Aplicația când regula depinde de un sistem pe care propoziția nu îl vede. Expresia de felul notei între A și F, care nu este nici cheie, nici NOT NULL, este CHECK din manualul 9.7; acest folder nu are un copil CHECK și nu predă expresia până la capăt.
   Demonstrație: secondaryIndexKeepsBothGrades

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE o înscriere o dată, iar cursul trebuie să existe -> registrationRulesTheServerEnforces -> PRIMARY KEY (student_id, course_id) și FOREIGN KEY (course_id) REFERENCES course (id) -> nu garantează că course există deja în tur: propoziția nu a fost trimisă
    2. Eșecul cel mai mic
           -> DECIDE IGNORE ca să treacă a doua notă -> insertIgnoreSkipsTheDuplicateGrade -> a doua pereche nu rămâne -> nu garantează un rând de eroare în client dacă avertismentul nu este citit
    3. Granița
           -> Regula este mecanismul MySQL. Șirul din Java nu este execuția. Click-ul din Workbench nu declară constrângerea. Numele coloanei course_id, fără CONSTRAINT, nu verifică părintele.
    4. Alternativa
           -> INDEX când duplicatele sunt legale. UNIQUE când NULL trebuie acceptat și cheia primară există deja. Trigger-ul când trebuie să ruleze cod, nu când trebuie să refuze orfanul.
    5. Anti-patternul
           -> INSERT IGNORE pe perechea repetată, sau CREATE TRIGGER care semnalează cursul lipsă în loc de FOREIGN KEY. Prima notă rămâne fără a doua, iar trigger-ul nu ține lacătul de referință.
    6. Dovada
           -> registrationRulesTheServerEnforces ține cele două CREATE. childCourseTheForeignKeyRefuses ține INSERT-ul cu 404. Planul nu a fost citit: root@localhost a fost refuzat.
    7. Interviu
           -> Serverul refuză rândul la sfârșitul propoziției. PRIMARY KEY pe pereche, FOREIGN KEY spre course. INSERT IGNORE ascunde a doua notă. Indexul pe grade nu refuză nimic.
*/

public class Constraints {

    /*
    🟢 Începător
        Ce este            regula care nu lasă înscrierea dublă și nici înscrierea la un curs care nu există
        De ce e nevoie     fără ea, a doua înscriere și cursul 404 rămân în tabelă
    🟡 Producție
        Cum se scrie       CREATE TABLE course_registration cu PRIMARY KEY (student_id, course_id) și FOREIGN KEY (course_id) REFERENCES course (id)
    🔴 Staff
        Unde               refuzul se vede la sfârșitul propoziției, pe InnoDB; indexul secundar nu refuză, trigger-ul nu ține locul cheii străine
    Pași
        1. Creează course cu PRIMARY KEY (id).
        2. Creează course_registration cu perechea ca PRIMARY KEY și cu FOREIGN KEY spre course.
        3. Nu trimite șirul din metodă: în tur, root@localhost a fost refuzat.
    */
    // Începător: metoda păstrează propozițiile prin care serverul primește regulile înscrierii.
    // Producție: public static String registrationRulesTheServerEnforces(). Dacă dispare, fast nu mai are alegerea unică.
    // Expert: Metoda nu deschide JDBC. Nu înlocuiește FOREIGN KEY cu un index și nu amână verificarea până la COMMIT.
    public static String registrationRulesTheServerEnforces() {
        // Începător: cursul trebuie să existe, iar același student nu rămâne înscris de două ori la el.
        // Producție: CREATE TABLE course și CREATE TABLE course_registration cu PRIMARY KEY și FOREIGN KEY. Fără șir, rămâne comentariul că orice constrângere doar leagă tabele.
        // Expert: grade rămâne NULL-abil, deci NOT NULL nu a fost pus pe notă. AUTO_INCREMENT nu este cheia acestei tabele. CHECK-ul pe valoarea notei nu este copilul acestui folder.
        return "CREATE TABLE course (id INT NOT NULL, PRIMARY KEY (id)) ENGINE=InnoDB; CREATE TABLE course_registration (student_id INT NOT NULL, course_id INT NOT NULL, grade CHAR(1) NULL, PRIMARY KEY (student_id, course_id), CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES course (id)) ENGINE=InnoDB";
        // Începător: metoda a înapoiat regulile, nu un rând de înscriere.
        // Producție: acolada închide registrationRulesTheServerEnforces. Șirul nu a fost trimis către 3306.
        // Expert: Refuzul root@localhost lasă efectul pe server NERULAT. Acolada nu este eroarea 1452, pentru că INSERT-ul cu 404 stă în cealaltă metodă.
    }

    /*
    🟢 Începător
        Ce este            înscrierea la cursul 404, care nu are voie să rămână dacă părintele lipsește
        De ce e nevoie     fără ea, pare că FOREIGN KEY se vede abia la COMMIT
    🟡 Producție
        Cum se scrie       INSERT INTO course_registration (student_id, course_id, grade) VALUES (1, 404, 'A')
    🔴 Staff
        Unde               pe InnoDB propoziția se dă înapoi imediat; un index pe course_id ar fi lăsat rândul
    Pași
        1. Ține INSERT-ul cu 404 separat de CREATE.
        2. Nu îl trimite în tur: autentificarea a căzut.
        3. Nu îl confunda cu încălcarea perechii, care este 1062, nu 1452.
    */
    // Începător: metoda păstrează înscrierea pe care cheia străină o poate opri.
    // Producție: public static String childCourseTheForeignKeyRefuses(). Dacă dispare, refuzul rămâne o poveste.
    // Expert: Șirul nu este un trigger și nu este INSERT IGNORE. Lacătul partajat de la verificare nu ține după ce clientul a primit eroarea.
    public static String childCourseTheForeignKeyRefuses() {
        // Începător: rândul cu cursul 404 nu rămâne în course_registration dacă regula este FOREIGN KEY și 404 nu este în course.
        // Producție: INSERT INTO course_registration (student_id, course_id, grade) VALUES (1, 404, 'A'). Dacă linia dispare, nu mai ai propoziția care se vede la sfârșit, nu la commit.
        // Expert: Eroarea de părinte lipsă nu este 1062 de la perechea repetată și nu este 1048 de la NOT NULL. foreign_key_checks=0 ar lăsa rândul, iar repornirea variabilei nu l-ar mai verifica.
        return "INSERT INTO course_registration (student_id, course_id, grade) VALUES (1, 404, 'A')";
        // Începător: metoda s-a oprit la înscrierea care nu are voie să rămână.
        // Producție: acolada închide childCourseTheForeignKeyRefuses. Șirul a rămas în proces.
        // Expert: Nu s-a citit un plan. Indexul secundar pe grade nu este acest INSERT.
    }

    /*
    🟢 Începător
        Ce este            două note pentru aceeași înscriere, din care a doua dispare pe tăcute
        De ce e nevoie     fără ea, IGNORE pare că păstrează ambele note
    🟡 Producție
        Cum se scrie       INSERT IGNORE INTO course_registration (student_id, course_id, grade) VALUES (1, 7, 'A'), (1, 7, 'B')
    🔴 Staff
        Unde               a doua pereche este sărită; prima rămâne. Nu este rollback-ul lui 404 și nu este un trigger
    Pași
        1. Ține ambele rânduri în aceeași propoziție.
        2. Nu o trimite în tur.
        3. Nu o folosi ca dovadă că FOREIGN KEY a rulat.
    */
    // Începător: metoda păstrează propoziția care pare că salvează a doua notă și nu o salvează.
    // Producție: public static String insertIgnoreSkipsTheDuplicateGrade(). Dacă dispare, eșecul rămâne doar în comentariu.
    // Expert: IGNORE sare peste încălcarea de PRIMARY KEY sau UNIQUE. Nu sare peste un curs inexistent în același fel și nu ține loc de SHOW WARNINGS.
    public static String insertIgnoreSkipsTheDuplicateGrade() {
        // Începător: prima notă poate rămâne, a doua notă pentru aceeași pereche nu.
        // Producție: INSERT IGNORE cu (1, 7, 'A') și (1, 7, 'B'). Dacă linia dispare, nu mai ai cel mai mic eșec de lângă CREATE.
        // Expert: Pe motorul netranzacțional, fără IGNORE, serverul se oprește la rândul greșit și lasă rândurile dinaintea lui. IGNORE nu transformă a doua notă în 'A'.
        return "INSERT IGNORE INTO course_registration (student_id, course_id, grade) VALUES (1, 7, 'A'), (1, 7, 'B')";
        // Începător: metoda a păstrat propoziția periculoasă, fără să scrie în tabelă din acest proces.
        // Producție: acolada închide insertIgnoreSkipsTheDuplicateGrade. Avertismentul nu a fost citit, pentru că șirul nu a plecat.
        // Expert: 1452 stă la INSERT-ul cu 404. Aici semnul este perechea sărită, nu părintele lipsă.
    }

    /*
    🟢 Începător
        Ce este            o cale de căutare pe notă, care lasă două note la fel
        De ce e nevoie     fără ea, orice index pare că refuză duplicatul
    🟡 Producție
        Cum se scrie       CREATE INDEX i_grade ON course_registration (grade)
    🔴 Staff
        Unde               planul poate folosi i_grade; rândurile cu aceeași notă rămân. Vecinul care refuză este PRIMARY KEY sau UNIQUE
    Pași
        1. Ține indexul separat de cheia primară.
        2. Nu îl trimite în tur.
        3. Nu îl numi constrângere doar pentru că stă în acest folder.
    */
    // Începător: metoda păstrează calea de căutare care nu apără nota.
    // Producție: public static String secondaryIndexKeepsBothGrades(). Dacă dispare, alternativa rămâne o frază.
    // Expert: i_grade nu este indexul grupat. În InnoDB, PRIMARY KEY este indexul grupat; indexul secundar nu refuză grade.
    public static String secondaryIndexKeepsBothGrades() {
        // Începător: două înscrieri pot avea nota A, iar căutarea după notă poate folosi indexul.
        // Producție: CREATE INDEX i_grade ON course_registration (grade). Dacă linia dispare, nu mai ai propoziția care nu refuză.
        // Expert: UNIQUE (grade) ar refuza a doua notă A. Acest index nu o refuză. EXPLAIN poate arăta i_grade și tot nu este o constrângere.
        return "CREATE INDEX i_grade ON course_registration (grade)";
        // Începător: metoda s-a oprit la calea de căutare.
        // Producție: acolada închide secondaryIndexKeepsBothGrades. Șirul nu a creat indexul pe server.
        // Expert: Folderul _3_Indexes predă indexul. Aici indexul este doar ce nu alegi când duplicatul trebuie să cadă.
    }

    /*
    🟢 Începător
        Ce este            un cod care se uită la curs înainte de înscriere și tot nu este regula
        De ce e nevoie     fără el, pare că semnalul din trigger înlocuiește părintele
    🟡 Producție
        Cum se scrie       CREATE TRIGGER registration_course_check BEFORE INSERT ON course_registration
    🔴 Staff
        Unde               trigger-ul rulează codul; nu pune lacătul de referință și nu este eroarea 1452
    Pași
        1. Ține trigger-ul separat de FOREIGN KEY.
        2. Nu îl trimite în tur.
        3. Nu îl folosi ca să sari FOREIGN KEY.
    */
    // Începător: metoda păstrează codul care pare că apără înscrierea și nu este constrângerea.
    // Producție: public static String triggerInsteadOfForeignKey(). Dacă dispare, vecinul rămâne doar un nume.
    // Expert: BEFORE INSERT vede rândul înainte să fie scris. Nu este ON DELETE RESTRICT și nu este foreign_key_checks.
    public static String triggerInsteadOfForeignKey() {
        // Începător: codul poate să se plângă că cursul lipsește, dar regula de părinte este tot FOREIGN KEY.
        // Producție: CREATE TRIGGER registration_course_check BEFORE INSERT ON course_registration FOR EACH ROW, cu SIGNAL dacă NEW.course_id nu este în course. Dacă linia dispare, nu mai ai vecinul lângă CONSTRAINT fk_course.
        // Expert: Un trigger nu ține locul lui FOREIGN KEY. SIGNAL este codul tău. 1452 este refuzul constrângerii. Cascade-ul de la ON DELETE nu pornește din acest trigger.
        return "CREATE TRIGGER registration_course_check BEFORE INSERT ON course_registration FOR EACH ROW BEGIN IF (SELECT id FROM course WHERE id = NEW.course_id) IS NULL THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'missing course'; END IF; END";
        // Începător: metoda a păstrat codul vecin, fără să declare cheia străină.
        // Producție: acolada închide triggerInsteadOfForeignKey. Trigger-ul nu a fost creat pe server.
        // Expert: Subcererea din trigger nu este un JOIN și nu este CHECK. CHECK nu poate ține o subcerere, după manualul 9.7, 15.1.25.6.
    }
}
