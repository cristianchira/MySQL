package _5_Relational_Schema;

/*                                       Relational Schema


    ○ the term of representing
                   * a table with the help of such
                                 - a rectangle
                                 - and the names of the fields
                         is called a relational schema

    ○ represent the concept database administrators must implement

    ○ depict how a database is organized

    = blueprints, or a plan for a database

    - will help you immensely while writing your queries!


                                          Database Schema


    ○ When you combine the relational schemas of the tables we have in a database this gives us a
                  * database schema informally.

    ○ It is OK to call the representation of the entire database in plural
                  * Relational Schemas.

 */

/*
TOPIC:
    Schema relațională este dicționarul de tabele, coloane, indexuri și chei străine. FOREIGN KEY pe sales.customer_id refuză vânzarea al cărei customer nu există. CREATE SCHEMA creează doar baza goală.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0
INTENT:
    O vânzare nu are voie să rămână cu un customer_id care lipsește din customer. După CONSTRAINT sales_ibfk_1, rândul cu părintele lipsă nu rămâne. KEY customer_id îl lasă.
MECHANISM:
    Serverul verifică FOREIGN KEY la INSERT sau UPDATE în sales și la DELETE sau UPDATE în customer. Pe InnoDB refuzul este la sfârșitul propoziției: clauza omisă este NO ACTION, echivalentă cu RESTRICT. INDEX și UNIQUE nu întreabă dacă părintele există.
LOOKALIKES:
    KEY customer_id și FOREIGN KEY (customer_id) stau pe aceeași coloană din sales. Indexul poate apărea în EXPLAIN. Doar FOREIGN KEY refuză orfanul. CREATE SCHEMA creează baza. PRIMARY KEY (purchase_number) identifică vânzarea. UNIQUE (email_address) refuză emailul nenul repetat și acceptă mai multe NULL.
BOOK THEORY:
    Manualul 9.7: conceptual, schema este mulțimea de tabele, coloane, indexuri și chei străine; fizic, CREATE SCHEMA este sinonim cu CREATE DATABASE. FOREIGN KEY stă pe tabela copil, respinge INSERT sau UPDATE dacă valoarea nu există la părinte, iar InnoDB dă propoziția înapoi. Fără ON DELETE, acțiunea este NO ACTION și ștergerea părintelui care încă are copii este respinsă. CASCADE șterge sau actualizează copiii potriviți în aceeași propoziție și, implicit, nu pornește trigger-e. UNIQUE respinge valoarea nenulă duplicat și permite mai multe NULL. Indexul obișnuit nu respinge duplicatul; 10.3.1 îl folosește la căutare, iar când se citesc majoritatea rândurilor citirea secvențială este mai rapidă. Foreign_Key.pdf spune că foreign key identifică relațiile dintre tabele, nu tabelele, și listează „no repeating and missing values” fără să lege fraza de partea părinte sau copil. Unique_Key_And_Null_Values.pdf spune că unique key se pune când nu vrei duplicate într-un câmp și listează NULL VALUES no/yes fără să lege „no” de o coloană. Relationships.pdf spune cât din foreign key se vede în primary key-ul părinte, cu one-to-many, one-to-one și many-to-many, și că schema relațională este planul pe care administratorul îl implementează.
BOOK EXAMPLE:
    RelationalSchema_Fast.childMustHaveParent
REPO DEMO:
    src/main/java/_5_Relational_Schema/Relational_Schema.java
BREAK LAB:
    SET foreign_key_checks = 0, apoi INSERT INTO sales (customer_id) VALUES (999). SHOW CREATE TABLE încă arată sales_ibfk_1, iar rândul orfan rămâne. Verificarea este oprită pe sesiune.
TRADE-OFFS:
    Orfanul permis la încărcare rămâne la KEY customer_id. Același customer o singură dată în sales cere UNIQUE peste FOREIGN KEY. O vânzare cu mai mulți clienți nu încape în sales.customer_id: tabela de legătură, cu două FOREIGN KEY.
ANTI-PATTERNS:
    KEY customer_id fără FOREIGN KEY, luat din exemplul sales ca și cum indexul ar verifica părintele. INSERT cu customer_id inexistent reușește.
STAFF QUESTIONS:
    Relational_Schema.saleMustNameExistingCustomer
    Relational_Schema.orphanSaleDoesNotStay
    Relational_Schema.indexDoesNotCheckCustomer
    Relational_Schema.checksOffKeepsOrphan
    Relational_Schema.uniqueOnForeignKeyIsOneToOne
    RelationalSchema_Staff_Interview, întrebările 1–50
NOVEL TRANSFER:
    Bilet și călător: ticket.passenger_id fără FOREIGN KEY păstrează biletul după ștergerea călătorului. FOREIGN KEY (passenger_id) REFERENCES passenger (passenger_id) refuză biletul orfan.
SPACED REVIEW:
    o zi: pe sales, numește obiectul care refuză customer_id 999
    șapte zile: pe același SHOW CREATE TABLE, separă KEY customer_id de CONSTRAINT sales_ibfk_1
    treizeci de zile: pe un domeniu nou, fără customer și sales, alege FOREIGN KEY, UNIQUE sau INDEX

BEST PRACTICES:
    1. Cazul corect
           -> vânzarea trebuie să numească un customer care există -> Relational_Schema.saleMustNameExistingCustomer -> CREATE TABLE sales (... CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE) -> nu garantează că EXPLAIN folosește indexul și nu pune CASCADE dacă ai omis clauza
    2. Eșecul cel mai mic
           -> verificarea oprită pe sesiune -> Relational_Schema.checksOffKeepsOrphan -> SET foreign_key_checks = 0 -> nu garantează că sales_ibfk_1 a dispărut din SHOW CREATE TABLE
    3. Granița
           -> FOREIGN KEY este regula InnoDB pe propoziția de scriere. Dreptunghiul din comentariul de sus și slide-ul Relationships.pdf sunt planul. JDBC și Workbench nu țin părintele dacă propoziția nu are constrângerea
    4. Alternativa
           -> orfan permis: KEY customer_id. Email nenul care nu se repetă, fără părinte: UNIQUE KEY unique_email_address (email_address). Unu-la-unu: FOREIGN KEY plus UNIQUE (customer_id). Mulți-la-mulți: tabelă de legătură
    5. Anti-patternul
           -> CREATE INDEX customer_id ON sales (customer_id) în locul lui FOREIGN KEY -> INSERT INTO sales (customer_id) VALUES (999) lasă rândul
    6. Dovada
           -> RelationalSchema_Fast.childMustHaveParent conține propoziția cu sales_ibfk_1. Foreign_Key.java o ține în comentariu și rămâne exemplul deja scris
    7. Interviu
           -> FOREIGN KEY refuză copilul fără părinte la sfârșitul INSERT pe InnoDB. KEY customer_id lasă orfanul. Fără ON DELETE CASCADE, DELETE pe customer-ul care încă are vânzări este respins

1. Ce problemă rezolvă mecanismul
       Enunț: De ce sales.customer_id nu ajunge ca să lege vânzarea de customer?
       Capcană: „Dacă coloana se numește customer_id, serverul știe că este cheie străină.”
       Cheie: Numele coloanei nu este în dicționar ca regulă. FOREIGN KEY (customer_id) REFERENCES customer (customer_id) este obiectul. Fără el, rândul cu un id inexistent rămâne.
       Demonstrație: Relational_Schema.saleMustNameExistingCustomer
2. Ce se întâmplă cu rândurile, schema, indexul sau tranzacția
       Enunț: Ce rămâne în sales după INSERT INTO sales (customer_id) VALUES (999) dacă 999 lipsește din customer și există sales_ibfk_1?
       Capcană: „Rândul se scrie și se șterge la COMMIT.”
       Cheie: Pe InnoDB propoziția este respinsă și dată înapoi la sfârșitul ei. Rândul nu rămâne. Schema nu se schimbă. La NDB, NO ACTION poate amâna verificarea până la commit.
       Demonstrație: Relational_Schema.orphanSaleDoesNotStay
3. De ce nu este mecanismul vecin
       Enunț: KEY customer_id (customer_id) și FOREIGN KEY sunt în același CREATE TABLE sales. De ce indexul nu este integritatea?
       Capcană: „Exemplul are KEY customer_id, deci indexul verifică părintele.”
       Cheie: Serverul cere un index pe coloana care referă ca să nu citească toată tabela la verificare. Indexul nu este regula. Fără CONSTRAINT, INSERT cu 999 reușește.
       Demonstrație: Relational_Schema.indexDoesNotCheckCustomer
4. Cum eșuează când este folosit greșit
       Enunț: FOREIGN KEY este în SHOW CREATE TABLE, dar orfanul s-a scris. Ce propoziție a lăsat rândul?
       Capcană: „Constrângerea s-a șters din dicționar.”
       Cheie: SET foreign_key_checks = 0 oprește verificarea pe sesiune. sales_ibfk_1 rămâne vizibil. INSERT-ul orfan rămâne în sales.
       Demonstrație: Relational_Schema.checksOffKeepsOrphan
5. Ce alegi în locul lui și în ce condiție
       Enunț: Un customer poate avea multe vânzări, iar o vânzare un singur customer. Când adaugi UNIQUE (customer_id) peste FOREIGN KEY?
       Capcană: „UNIQUE este același lucru cu FOREIGN KEY.”
       Cheie: FOREIGN KEY singur lasă același customer_id de mai multe ori în sales: unul-la-mulți. UNIQUE (customer_id) îl lasă o singură dată pentru valori nenule și încă permite mai multe NULL dacă coloana este nulabilă. Unu-la-unu este ambele. Mulți-la-mulți nu încape în coloană.
       Demonstrație: Relational_Schema.uniqueOnForeignKeyIsOneToOne
*/

/*
🟢 Începător
    Ce este            schema relațională ține tabelele și regula dintre customer și sales. FOREIGN KEY lasă vânzarea doar dacă customer_id există la părinte
    De ce e nevoie     fără constrângere, vânzarea cu un customer_id inventat rămâne în sales
🟡 Producție
    Cum se scrie       CREATE TABLE sales (... KEY customer_id (customer_id), CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE)
🔴 Staff
    Unde               refuzul se vede la sfârșitul INSERT pe InnoDB. KEY customer_id este indexul, nu regula. CREATE SCHEMA nu creează legătura
Pași                   1. propoziția care pune sales_ibfk_1
                       2. INSERT-ul orfan care nu lasă rândul
                       3. indexul care lasă orfanul
                       4. foreign_key_checks oprit
                       5. UNIQUE peste FOREIGN KEY, unu-la-unu
*/
public class Relational_Schema {

    // Începător: metoda predă regula prin care vânzarea rămâne doar lângă un customer care există.
    // Producție: saleMustNameExistingCustomer întoarce CREATE TABLE sales cu sales_ibfk_1. Fără această metodă, întrebarea 1 nu are propoziția.
    // Expert: metoda nu deschide sesiunea și nu atinge foreign_key_checks. CREATE SCHEMA shop tot lasă customer și sales nelegate.
    public static String saleMustNameExistingCustomer() {
        // Începător: în sales rămâne o vânzare doar dacă customer_id este deja în customer. Id-ul inventat nu are loc în tabelă.
        // Producție: CREATE TABLE sales (purchase_number INT NOT NULL AUTO_INCREMENT, date_of_purchase DATE DEFAULT NULL, customer_id INT DEFAULT NULL, item_code VARCHAR(10) DEFAULT NULL, PRIMARY KEY (purchase_number), KEY customer_id (customer_id), CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE) ENGINE=InnoDB. Fără CONSTRAINT, INSERT cu 999 rămâne.
        // Expert: KEY customer_id din aceeași propoziție este indexul cu care verificarea evită citirea întregii tabele. Omisiunea lui ON DELETE este NO ACTION, nu CASCADE.
        return "CREATE TABLE sales (purchase_number INT NOT NULL AUTO_INCREMENT, date_of_purchase DATE DEFAULT NULL, customer_id INT DEFAULT NULL, item_code VARCHAR(10) DEFAULT NULL, PRIMARY KEY (purchase_number), KEY customer_id (customer_id), CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE) ENGINE=InnoDB";
    // Începător: metoda care predă CREATE TABLE sales s-a închis. Niciun rând nu s-a scris încă în sales.
    // Producție: fără acolada metodei, propoziția cu sales_ibfk_1 nu mai poate fi citită din saleMustNameExistingCustomer.
    // Expert: acolada nu face COMMIT și nu șterge customer. Efectul asupra rândului se vede la INSERT-ul următor, pe InnoDB imediat.
    }

    // Începător: metoda predă scrierea unei vânzări al cărei customer nu există.
    // Producție: orphanSaleDoesNotStay întoarce INSERT INTO sales (customer_id) VALUES (999). Fără metodă, întrebarea 2 nu are propoziția.
    // Expert: 999 nu este un customer din curs. Pe InnoDB, cu sales_ibfk_1, propoziția se dă înapoi la sfârșit. La NDB, NO ACTION poate aștepta commit-ul.
    public static String orphanSaleDoesNotStay() {
        // Începător: vânzarea cu customer_id 999 nu rămâne în sales dacă acest id lipsește din customer.
        // Producție: INSERT INTO sales (customer_id) VALUES (999). Cu sales_ibfk_1, InnoDB respinge propoziția și o dă înapoi. Fără FOREIGN KEY, rândul rămâne.
        // Expert: PRIMARY KEY (purchase_number) nu este regula care citește customer. foreign_key_checks = 0 lasă același INSERT să scrie orfanul, cu constrângerea încă în dicționar.
        return "INSERT INTO sales (customer_id) VALUES (999)";
    // Începător: metoda INSERT-ului orfan s-a închis. Rândul din sales nu a fost creat de acoladă.
    // Producție: fără închidere, orphanSaleDoesNotStay nu compilează și dispare propoziția de la întrebarea 2.
    // Expert: închiderea nu este ROLLBACK. Darea înapoi o face InnoDB la sfârșitul INSERT-ului care încalcă FOREIGN KEY.
    }

    // Începător: metoda predă indexul de pe customer_id, care lasă vânzarea și când customer lipsește.
    // Producție: indexDoesNotCheckCustomer întoarce CREATE INDEX customer_id ON sales (customer_id). Fără metodă, întrebarea 3 nu are vecinul.
    // Expert: acesta este copilul regular index. Nu are REFERENCES. EXPLAIN poate arăta indexul și tot nu dovedește sales_ibfk_1.
    public static String indexDoesNotCheckCustomer() {
        // Începător: sales poate căpăta un index pe customer_id, iar vânzarea cu un id inexistent rămâne.
        // Producție: CREATE INDEX customer_id ON sales (customer_id). Dispare regula dacă în loc de FOREIGN KEY rămâne doar această propoziție: INSERT cu 999 reușește.
        // Expert: indexul secundar nu respinge duplicatul. UNIQUE (email_address) respinge emailul nenul repetat și tot nu caută un părinte. Citirea majorității rândurilor poate ocoli indexul.
        return "CREATE INDEX customer_id ON sales (customer_id)";
    // Începător: metoda indexului s-a închis. Rândurile din sales au rămas cum erau.
    // Producție: fără închidere, CREATE INDEX customer_id nu mai este disponibil pentru întrebarea 3.
    // Expert: închiderea nu face indexul invizibil și nu oprește optimizerul. Vizibilitatea se scrie la CREATE INDEX, nu aici.
    }

    // Începător: metoda predă sesiunea în care vânzarea orfană se scrie deși regula există în dicționar.
    // Producție: checksOffKeepsOrphan întoarce SET foreign_key_checks = 0. Fără metodă, întrebarea 4 nu are propoziția greșită.
    // Expert: mysqldump oprește verificarea ca să încarce tabelele în orice ordine. Lăsată pornită pe sesiunea de lucru, sales_ibfk_1 rămâne în SHOW CREATE TABLE și orfanul rămâne în sales.
    public static String checksOffKeepsOrphan() {
        // Începător: în această sesiune, vânzarea cu customer_id inexistent se scrie chiar dacă legătura este definită.
        // Producție: SET foreign_key_checks = 0. Fără ea, INSERT INTO sales (customer_id) VALUES (999) este respins când sales_ibfk_1 este activ.
        // Expert: setarea nu șterge CONSTRAINT și nu ține până la o altă conexiune. INSERT IGNORE, pe pagina de constrângeri, sare peste PRIMARY KEY și UNIQUE, nu este această setare.
        return "SET foreign_key_checks = 0";
    // Începător: metoda setării s-a închis. Dicționarul din sales nu s-a schimbat prin acoladă.
    // Producție: fără închidere, checksOffKeepsOrphan nu mai predă propoziția care lasă orfanul.
    // Expert: închiderea nu pune foreign_key_checks înapoi la 1. Următoarea propoziție din aceeași sesiune încă sare verificarea.
    }

    // Începător: metoda predă pasul prin care același customer rămâne o singură dată în sales.
    // Producție: uniqueOnForeignKeyIsOneToOne întoarce ALTER TABLE sales ADD UNIQUE KEY one_customer_once (customer_id). Fără metodă, întrebarea 5 nu are alegerea.
    // Expert: FOREIGN KEY singur este unul-la-mulți, fiindcă customer_id se repetă în sales. UNIQUE pe aceeași coloană este unu-la-unu. Mulți-la-mulți cere o tabelă de legătură, nu a doua coloană în sales.
    public static String uniqueOnForeignKeyIsOneToOne() {
        // Începător: după această schimbare, un customer nenul are o singură vânzare în sales. A doua vânzare cu același id nu rămâne.
        // Producție: ALTER TABLE sales ADD UNIQUE KEY one_customer_once (customer_id). Fără UNIQUE, același customer_id poate sta pe mai multe rânduri sales cât timp FOREIGN KEY găsește părintele.
        // Expert: UNIQUE permite mai multe NULL pe customer_id dacă coloana este nulabilă. PRIMARY KEY (purchase_number) rămâne identitatea vânzării. Nu înlocui purchase_number cu customer_id doar ca să iasă unu-la-unu.
        return "ALTER TABLE sales ADD UNIQUE KEY one_customer_once (customer_id)";
    // Începător: metoda unu-la-unu s-a închis. Rândurile vechi din sales nu s-au unit prin acoladă.
    // Producție: fără închidere, uniqueOnForeignKeyIsOneToOne nu compilează și dispare propoziția de la întrebarea 5.
    // Expert: închiderea nu verifică duplicatele deja scrise. ALTER eșuează dacă același customer_id nenul este deja de două ori în sales.
    }
// Începător: clasa schemei s-a închis. Nici customer, nici sales nu s-au schimbat prin închidere.
// Producție: fără acolada clasei, cele cinci propoziții nu mai compilează.
// Expert: închiderea nu execută SET foreign_key_checks și nu ține un lacăt după un SELECT. Refuzul orfanului stă pe INSERT, pe InnoDB la sfârșitul propoziției.
}
