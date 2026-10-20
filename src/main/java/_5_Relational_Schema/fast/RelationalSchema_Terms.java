/*
Definiția unei scheme relaționale (relational schema):
       Conceptual, schema este mulțimea de tabele, coloane, indexuri și chei străine. În MySQL, CREATE SCHEMA este sinonim cu CREATE DATABASE și lasă baza goală.

Exemplu:
       RelationalSchema_Terms.explainRelationalSchema()
       Legătura dintre sales și customer este sales_ibfk_1, nu numele bazei.

Definiția unei chei străine (foreign key):
       Cheia străină stă pe tabela copil și respinge valoarea care nu există la părinte.

Exemplu:
       RelationalSchema_Terms.explainForeignKey()
       O vânzare cu customer_id 999 nu rămâne dacă 999 lipsește din customer.

Definiția unei tabele părinte (parent table):
       Tabela părinte ține valoarea pe care copilul o caută. Ștergerea ei depinde de ON DELETE.

Exemplu:
       RelationalSchema_Terms.explainParentTable()
       DELETE pe customer cu vânzări încă legate este respins dacă ON DELETE lipsește.

Definiția unei tabele copil (child table):
       Tabela copil este sales, cea care poartă FOREIGN KEY (customer_id).

Exemplu:
       RelationalSchema_Terms.explainChildTable()
       INSERT-ul în sales este propoziția la care se vede orfanul respins.

Definiția unui index unic (unique index):
       UNIQUE respinge valoarea nenulă duplicat și permite mai multe NULL.

Exemplu:
       RelationalSchema_Terms.explainUniqueIndex()
       Al doilea users.email_address identic nu rămâne. Două emailuri NULL rămân.

Definiția unui index secundar (secondary index):
       Indexul obișnuit, KEY sau CREATE INDEX, nu respinge duplicatul și nu caută un părinte.

Exemplu:
       RelationalSchema_Terms.explainSecondaryIndex()
       CREATE INDEX email_index lasă două emailuri identice în users.

Definiția unei relații (relational):
       Unu-la-mulți este FOREIGN KEY fără UNIQUE pe sales.customer_id. Unu-la-unu adaugă UNIQUE. Serverul nu are o propoziție RELATIONSHIP.

Exemplu:
       RelationalSchema_Terms.explainRelationship()
       Același customer_id din customer poate apărea de mai multe ori în sales.

Terms & Definitions:
*/

// Începător: pachetul ține termenii schemei, fără să creeze el însuși customer sau sales.
// Producție: package _5_Relational_Schema.fast. Fără pachet, metodele glosarului nu stau lângă lecție.
// Expert: pachetul nu este sinonimul CREATE SCHEMA. Serverul nu citește numele pachetului când verifică sales_ibfk_1.
package _5_Relational_Schema.fast;

/*
🟢 Începător
    Ce este            glosarul schemei: obiectele dintre customer, sales și users, cu propoziția fiecăruia
    De ce e nevoie     fără propoziție, numele customer_id pare deja o cheie străină
🟡 Producție
    Cum se scrie       fiecare metodă întoarce SQL gata de trimis, pe vocabularul sales, customer și users
🔴 Staff
    Unde               efectul se vede la sfârșitul propoziției de scriere pe InnoDB. CREATE SCHEMA nu leagă tabelele
Pași                   1. schema relațională
                       2. foreign key
                       3. tabela părinte
                       4. tabela copil
                       5. unique index
                       6. secondary index
                       7. relația
*/
// Începător: clasa dă propoziția fiecărui obiect din schema relațională a cursului.
// Producție: public class RelationalSchema_Terms. Fără clasă, exemplele din definiții nu au metodă.
// Expert: clasa nu copiază Foreign_Key.java. Propozițiile noi folosesc aceleași tabele: sales, customer, users.
public class RelationalSchema_Terms {

    /*
    🟢 Începător
        Ce este            schema relațională legată: sales îl caută pe customer prin sales_ibfk_1
        De ce e nevoie     CREATE SCHEMA shop lasă tabelele nelegate
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (... CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE) ENGINE=InnoDB
    🔴 Staff
        Unde               obiectul este în dicționar după CREATE TABLE. Baza creată cu CREATE SCHEMA nu conține regula
    Pași                   1. întoarce CREATE TABLE sales
    */
    // Începător: metoda dă propoziția prin care sales și customer devin o schemă legată.
    // Producție: explainRelationalSchema întoarce CREATE TABLE sales cu sales_ibfk_1. Fără metodă, definiția nu are exemplu executabil.
    // Expert: CREATE SCHEMA shop este sinonimul lui CREATE DATABASE. Nu scrie REFERENCES customer. Vecinul index, KEY customer_id, stă în aceeași propoziție și nu verifică părintele.
    public static String explainRelationalSchema() {
        // Începător: apare tabela sales, iar vânzarea rămâne doar dacă customer_id există în customer. Ștergerea customer-ului ia și vânzările lui.
        // Producție: CREATE TABLE sales (purchase_number INT NOT NULL AUTO_INCREMENT, date_of_purchase DATE DEFAULT NULL, customer_id INT DEFAULT NULL, item_code VARCHAR(10) DEFAULT NULL, PRIMARY KEY (purchase_number), KEY customer_id (customer_id), CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE) ENGINE=InnoDB. Fără CONSTRAINT, id-ul inventat rămâne.
        // Expert: PRIMARY KEY (purchase_number) identifică vânzarea în InnoDB ca index grupat. Nu citește customer. Omisiunea ON DELETE este NO ACTION, nu CASCADE.
        return "CREATE TABLE sales (purchase_number INT NOT NULL AUTO_INCREMENT, date_of_purchase DATE DEFAULT NULL, customer_id INT DEFAULT NULL, item_code VARCHAR(10) DEFAULT NULL, PRIMARY KEY (purchase_number), KEY customer_id (customer_id), CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE) ENGINE=InnoDB";
    // Începător: metoda schemei s-a închis. Baza nu s-a creat prin acoladă.
    // Producție: fără închidere, explainRelationalSchema nu compilează.
    // Expert: închiderea nu schimbă foreign_key_checks. Dicționarul se schimbă când serverul execută șirul.
    }

    /*
    🟢 Începător
        Ce este            regula de pe sales care respinge customer_id lipsă din customer
        De ce e nevoie     coloana singură păstrează orfanul
    🟡 Producție
        Cum se scrie       ALTER TABLE sales ADD CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE
    🔴 Staff
        Unde               verificarea este la scriere. Indexul de pe coloană poate fi creat automat și tot nu este regula
    Pași                   1. întoarce ALTER TABLE sales
    */
    // Începător: metoda dă regula care se adaugă pe o tabelă sales deja existentă.
    // Producție: explainForeignKey întoarce ALTER TABLE cu sales_ibfk_1. Fără metodă, termenul foreign key nu are propoziție.
    // Expert: părintele și copilul trebuie să aibă același motor și să nu fie tabele temporare. O referință către un index părinte care nu este unic este învechită fără restrict_fk_on_non_standard_key.
    public static String explainForeignKey() {
        // Începător: sales începe să respingă vânzarea al cărei customer nu există. Dacă customer-ul este șters, vânzările lui sunt șterse odată cu el.
        // Producție: ALTER TABLE sales ADD CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE. Fără REFERENCES, customer_id rămâne o coloană.
        // Expert: serverul creează un index pe coloana copil dacă lipsește, ca să nu citească toată tabela la verificare. Acest index poate fi abandonat mai târziu dacă alt index poate impune aceeași constrângere.
        return "ALTER TABLE sales ADD CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE";
    // Începător: metoda cheii străine s-a închis. Niciun rând din sales nu s-a șters prin acoladă.
    // Producție: fără închidere, explainForeignKey nu compilează.
    // Expert: închiderea nu pornește trigger-ele. Cascada, implicit, nu pornește trigger-e pe copii.
    }

    /*
    🟢 Începător
        Ce este            tabela customer, cea care ține customer_id căutat de vânzări
        De ce e nevoie     fără rândul părinte, vânzarea legată nu are voie să rămână
    🟡 Producție
        Cum se scrie       DELETE FROM customer WHERE customer_id = 7
    🔴 Staff
        Unde               fără ON DELETE, InnoDB respinge ștergerea imediat dacă există o vânzare cu 7. CASCADE o ia în aceeași propoziție
    Pași                   1. întoarce DELETE pe customer
    */
    // Începător: metoda dă ștergerea unui customer care poate avea vânzări.
    // Producție: explainParentTable întoarce DELETE FROM customer WHERE customer_id = 7. Fără metodă, termenul parent table nu are propoziție.
    // Expert: ON DELETE SET NULL cere ca sales.customer_id să permită NULL. SET DEFAULT este recunoscut de parser și respins de InnoDB la definirea tabelei.
    public static String explainParentTable() {
        // Începător: se încearcă scoaterea customer-ului 7. Vânzările care încă îl numesc decid dacă el dispare.
        // Producție: DELETE FROM customer WHERE customer_id = 7. Cu ON DELETE CASCADE pe sales_ibfk_1, vânzările cu 7 dispar în aceeași propoziție. Fără clauză, ștergerea este respinsă și customer rămâne.
        // Expert: cascada are o adâncime de cel mult 15 de niveluri. Peste limită, propoziția eșuează cu ER_FK_DEPTH_EXCEEDED. Nu rămâne o ștergere pe jumătate acceptată ca succes.
        return "DELETE FROM customer WHERE customer_id = 7";
    // Începător: metoda părintelui s-a închis. customer 7 nu a fost șters de acoladă.
    // Producție: fără închidere, explainParentTable nu compilează.
    // Expert: închiderea nu amână verificarea până la COMMIT. Pe InnoDB, NO ACTION respinge imediat.
    }

    /*
    🟢 Începător
        Ce este            tabela sales, cea care primește vânzarea și poartă customer_id
        De ce e nevoie     aici se vede dacă orfanul rămâne sau nu
    🟡 Producție
        Cum se scrie       INSERT INTO sales (customer_id) VALUES (999)
    🔴 Staff
        Unde               cu sales_ibfk_1, InnoDB dă propoziția înapoi la sfârșit. Indexul singur lasă rândul
    Pași                   1. întoarce INSERT în sales
    */
    // Începător: metoda dă scrierea unei vânzări în tabela copil.
    // Producție: explainChildTable întoarce INSERT INTO sales (customer_id) VALUES (999). Fără metodă, termenul child table nu are propoziție.
    // Expert: INSERT IGNORE, pe pagina de PRIMARY KEY și UNIQUE, sare peste acele încălcări. Pagina nu spune că sare peste FOREIGN KEY.
    public static String explainChildTable() {
        // Începător: se scrie o vânzare cu customer_id 999. Dacă 999 nu este în customer, vânzarea nu rămâne când regula este activă.
        // Producție: INSERT INTO sales (customer_id) VALUES (999). Cu sales_ibfk_1, InnoDB respinge propoziția. Fără FOREIGN KEY, rândul rămâne.
        // Expert: SET foreign_key_checks = 0 lasă același INSERT să scrie orfanul, iar SHOW CREATE TABLE încă arată constrângerea. Setarea este pe sesiune.
        return "INSERT INTO sales (customer_id) VALUES (999)";
    // Începător: metoda copilului s-a închis. Rândul din sales nu a fost scris de acoladă.
    // Producție: fără închidere, explainChildTable nu compilează.
    // Expert: închiderea nu ține lacăt după un SELECT care a plecat deja. Refuzul stă pe această scriere.
    }

    /*
    🟢 Începător
        Ce este            regula de pe users.email_address: același email nenul nu se repetă
        De ce e nevoie     fără UNIQUE, două conturi pot avea același email
    🟡 Producție
        Cum se scrie       CREATE TABLE users (id INT PRIMARY KEY, username VARCHAR(255), email_address VARCHAR(255), UNIQUE KEY unique_email_address (email_address))
    🔴 Staff
        Unde               duplicatul nenul eșuează la INSERT. Mai multe NULL rămân. Nu există REFERENCES, deci nu este cheie străină
    Pași                   1. întoarce CREATE TABLE users
    */
    // Începător: metoda dă tabela users în care emailul nenul apare o singură dată.
    // Producție: explainUniqueIndex întoarce CREATE TABLE users cu unique_email_address. Fără metodă, termenul unique index nu are propoziția din curs.
    // Expert: dacă nu există PRIMARY KEY, InnoDB poate promova primul UNIQUE NOT NULL la cheie primară. UNIQUE care permite NULL nu devine PRIMARY KEY.
    public static String explainUniqueIndex() {
        // Începător: se creează users. Al doilea rând cu același email nu rămâne. Două emailuri goale, necunoscute, pot rămâne.
        // Producție: CREATE TABLE users (id INT PRIMARY KEY, username VARCHAR(255), email_address VARCHAR(255), UNIQUE KEY unique_email_address (email_address)). Fără UNIQUE, emailul repetat rămâne.
        // Expert: PRIMARY KEY este una și nu acceptă NULL. Aici pot exista mai multe UNIQUE. INSERT IGNORE sare peste duplicatul de UNIQUE. Nu transformă emailul într-o cheie străină.
        return "CREATE TABLE users (id INT PRIMARY KEY, username VARCHAR(255), email_address VARCHAR(255), UNIQUE KEY unique_email_address (email_address))";
    // Începător: metoda indexului unic s-a închis. Niciun email nu a fost scris prin acoladă.
    // Producție: fără închidere, explainUniqueIndex nu compilează.
    // Expert: închiderea nu umple NULL-urile. Două NULL pe email_address rămân permise de UNIQUE.
    }

    /*
    🟢 Începător
        Ce este            un index pe email, care ajută căutarea și lasă emailurile repetate
        De ce e nevoie     fără să-l separi de UNIQUE, a doua adresă identică pare interzisă
    🟡 Producție
        Cum se scrie       CREATE INDEX email_index ON users (email_address)
    🔴 Staff
        Unde               se adaugă un index secundar. Rândurile nu se schimbă. Optimizerul poate citi secvențial când atinge majoritatea rândurilor
    Pași                   1. întoarce CREATE INDEX email_index
    */
    // Începător: metoda dă indexul care nu interzice emailul repetat.
    // Producție: explainSecondaryIndex întoarce CREATE INDEX email_index ON users (email_address). Fără metodă, termenul secondary index nu are propoziția din Regular_Index.
    // Expert: un index invizibil nu este folosit de optimizer. Vizibilitatea nu oprește o FOREIGN KEY de pe altă coloană. Pe email_index nu există REFERENCES.
    public static String explainSecondaryIndex() {
        // Începător: users capătă un index pe email. Două rânduri cu același email rămân.
        // Producție: CREATE INDEX email_index ON users (email_address). Fără UNIQUE în această propoziție, al doilea email identic se scrie.
        // Expert: 10.3.1 spune că indexul găsește rânduri repede, iar când interogarea citește majoritatea rândurilor citirea secvențială este mai rapidă. Indexul nu respinge orfanul din sales.
        return "CREATE INDEX email_index ON users (email_address)";
    // Începător: metoda indexului secundar s-a închis. Emailurile din users au rămas neschimbate.
    // Producție: fără închidere, explainSecondaryIndex nu compilează.
    // Expert: închiderea nu face indexul acoperitor. Acoperirea ține de coloanele cerute de SELECT, nu de această acoladă.
    }

    /*
    🟢 Începător
        Ce este            unul-la-mulți dintre customer și sales: același client, mai multe vânzări
        De ce e nevoie     fără să știi cardinalitatea, UNIQUE pe customer_id taie a doua vânzare
    🟡 Producție
        Cum se scrie       ALTER TABLE sales ADD UNIQUE KEY one_customer_once (customer_id)
    🔴 Staff
        Unde               UNIQUE schimbă unul-la-mulți în unu-la-unu. Many-to-many nu încape în coloană
    Pași                   1. întoarce ALTER TABLE care strânge customer_id la o apariție
    */
    // Începător: metoda arată cum același customer ajunge să aibă o singură vânzare.
    // Producție: explainRelationship întoarce ALTER TABLE sales ADD UNIQUE KEY one_customer_once (customer_id). Fără metodă, cardinalitatea nu are propoziție.
    // Expert: Relationships.pdf numește one-to-many, one-to-one și many-to-many. Serverul nu are cuvântul RELATIONSHIP. Unu-la-mulți este FOREIGN KEY fără acest UNIQUE.
    public static String explainRelationship() {
        // Începător: după schimbare, un customer nenul are o singură vânzare. Înainte, același customer_id putea sta pe mai multe rânduri sales.
        // Producție: ALTER TABLE sales ADD UNIQUE KEY one_customer_once (customer_id). Fără acest UNIQUE, relația din curs rămâne one-to-many: un customer_id din customer apare de multe ori în sales.
        // Expert: many-to-many nu se scrie cu a doua coloană în sales. Cere o tabelă de legătură și două FOREIGN KEY. UNIQUE încă permite mai multe NULL dacă customer_id este nulabil.
        return "ALTER TABLE sales ADD UNIQUE KEY one_customer_once (customer_id)";
    // Începător: metoda relației s-a închis. Vânzările vechi nu s-au unit prin acoladă.
    // Producție: fără închidere, explainRelationship nu compilează.
    // Expert: ALTER eșuează dacă customer_id nenul este deja duplicat în sales. Închiderea nu face ea însăși verificarea.
    }
// Începător: glosarul schemei s-a închis. Niciun rând din customer, sales sau users nu s-a schimbat.
// Producție: fără acolada clasei, RelationalSchema_Terms nu compilează și exemplele din definiții rămân fără metodă.
// Expert: închiderea nu creează tabela de legătură pentru many-to-many și nu pornește foreign_key_checks.
}
