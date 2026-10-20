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
    Manualul 9.7: conceptual, schema este mulțimea de tabele, coloane, indexuri și chei străine; fizic, CREATE SCHEMA este sinonim cu CREATE DATABASE. FOREIGN KEY stă pe tabela copil, respinge INSERT sau UPDATE dacă valoarea nu există la părinte, iar InnoDB dă propoziția înapoi. Fără ON DELETE, acțiunea este NO ACTION și ștergerea părintelui care încă are copii este respinsă. CASCADE șterge sau actualizează copiii potriviți în aceeași propoziție și, implicit, nu pornește trigger-e. UNIQUE respinge valoarea nenulă duplicat și permite mai multe NULL. Indexul obișnuit nu respinge duplicatul; 10.3.1 îl folosește la căutare, iar când se citesc majoritatea rândurilor citirea secvențială este mai rapidă. Foreign_Key.pdf spune că foreign key identifică relațiile dintre tabele, nu tabelele. Unique_Key_And_Null_Values.pdf spune că unique key se pune când nu vrei duplicate într-un câmp. Relationships.pdf spune cât din foreign key se vede în primary key-ul părinte și că schema relațională este planul pe care administratorul îl implementează.
BOOK EXAMPLE:
    RelationalSchema_Fast.childMustHaveParent
REPO DEMO:
    src/main/java/_5_Relational_Schema/fast/RelationalSchema_Fast.java
BREAK LAB:
    SET foreign_key_checks = 0, apoi INSERT INTO sales (customer_id) VALUES (999). SHOW CREATE TABLE încă arată sales_ibfk_1, iar rândul orfan rămâne.
TRADE-OFFS:
    Orfanul permis la încărcare rămâne la KEY customer_id. Același customer o singură dată în sales cere UNIQUE peste FOREIGN KEY. O vânzare cu mai mulți clienți nu încape în sales.customer_id: tabela de legătură, cu două FOREIGN KEY.
ANTI-PATTERNS:
    KEY customer_id fără FOREIGN KEY. INSERT cu customer_id inexistent reușește.
STAFF QUESTIONS:
    RelationalSchema_Staff_Interview, întrebările 1–50
NOVEL TRANSFER:
    Bilet și călător: FOREIGN KEY (passenger_id) REFERENCES passenger (passenger_id) refuză biletul al cărui călător nu există.
SPACED REVIEW:
    o zi: pe sales, numește obiectul care refuză customer_id 999
    șapte zile: separă KEY customer_id de CONSTRAINT sales_ibfk_1
    treizeci de zile: pe un domeniu nou, alege FOREIGN KEY, UNIQUE sau INDEX

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
*/

// Începător: pachetul ține lecția scurtă a schemei, lângă customer și sales, fără să creeze baza.
// Producție: package _5_Relational_Schema.fast. Fără el, RelationalSchema_Fast nu se așază în folderul fast și propoziția nu mai are locul ei.
// Expert: pachetul nu este CREATE SCHEMA și nu este o constrângere. Serverul nu vede pachetul Java când verifică sales.customer_id.
package _5_Relational_Schema.fast;

/*
🟢 Începător
    Ce este            lecția alege obiectul din schema relațională: FOREIGN KEY pe sales.customer_id ține vânzarea lângă un customer care există
    De ce e nevoie     fără sales_ibfk_1, vânzarea cu un customer inexistent rămâne
🟡 Producție
    Cum se scrie       CREATE TABLE sales (... KEY customer_id (customer_id), CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE) ENGINE=InnoDB
🔴 Staff
    Unde               pe InnoDB refuzul este la sfârșitul scrierii. KEY customer_id nu verifică customer. CREATE SCHEMA nu creează legătura
Pași                   1. main tipărește propoziția
                       2. childMustHaveParent ține CREATE TABLE sales cu sales_ibfk_1
*/
// Începător: clasa ține exemplul minim prin care vânzarea cere un customer care există.
// Producție: public class RelationalSchema_Fast. Fără clasă, childMustHaveParent nu are unde să întoarcă sales_ibfk_1.
// Expert: clasa nu înlocuiește Foreign_Key.java. Comentariul de acolo rămâne. Aici propoziția stă într-un șir gata de trimis.
public class RelationalSchema_Fast {

    /*
    🟢 Începător
        Ce este            main tipărește propoziția schemei, ca să o vezi înainte de server
        De ce e nevoie     fără main, exemplul minim nu are intrare
    🟡 Producție
        Cum se scrie       System.out.println(childMustHaveParent())
    🔴 Staff
        Unde               consola arată textul. Customer și sales nu se schimbă până când serverul primește șirul
    Pași                   1. citește șirul din childMustHaveParent
                           2. îl tipărește
    */
    // Începător: pornește exemplul care arată cum se leagă sales de customer.
    // Producție: public static void main(String[] args). Fără main, nu există o intrare care să tipărească sales_ibfk_1.
    // Expert: main nu deschide o conexiune și nu pune foreign_key_checks. Lipsa unui server în tur lasă rularea neexecutată.
    public static void main(String[] args) {
        // Începător: pe ecran apare propoziția care creează sales cu legătura către customer.
        // Producție: System.out.println(childMustHaveParent()). Fără apel, CREATE TABLE sales cu sales_ibfk_1 rămâne nemaivăzut.
        // Expert: println nu trimite propoziția la server. KEY customer_id din șir nu verifică părintele. O face CONSTRAINT.
        System.out.println(childMustHaveParent());
    // Începător: exemplul tipărit s-a închis. Niciun rând nu a intrat în sales.
    // Producție: fără acolada din main, clasa nu compilează și dispare intrarea care arată propoziția.
    // Expert: închiderea nu este COMMIT. Tabela sales apare abia când serverul execută șirul.
    }

    /*
    🟢 Începător
        Ce este            propoziția care lasă în sales doar vânzarea al cărei customer există, și șterge vânzările odată cu customer-ul
        De ce e nevoie     fără CONSTRAINT sales_ibfk_1, customer_id inventat rămâne
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (... CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE) ENGINE=InnoDB
    🔴 Staff
        Unde               regula se vede la INSERT în sales și la DELETE în customer. Indexul KEY customer_id este vecinul care nu refuză orfanul
    Pași                   1. întoarce propoziția din exemplul sales
    */
    // Începător: metoda dă propoziția prin care vânzarea rămâne legată de un customer.
    // Producție: childMustHaveParent întoarce CREATE TABLE sales cu sales_ibfk_1. Fără metodă, lecția nu are șirul de trimis la server.
    // Expert: vecinul este CREATE INDEX customer_id ON sales (customer_id), care lasă orfanul. Metoda aceasta îl numește o dată și nu îl pune în șir.
    public static String childMustHaveParent() {
        // Începător: se creează tabela sales, iar o vânzare al cărei customer lipsește nu are voie să rămână. Dacă customer-ul dispare, vânzările lui dispar odată cu el.
        // Producție: CREATE TABLE sales (purchase_number INT NOT NULL AUTO_INCREMENT, date_of_purchase DATE DEFAULT NULL, customer_id INT DEFAULT NULL, item_code VARCHAR(10) DEFAULT NULL, PRIMARY KEY (purchase_number), KEY customer_id (customer_id), CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE) ENGINE=InnoDB. Fără CONSTRAINT, INSERT cu 999 rămâne.
        // Expert: KEY customer_id nu caută în customer. Fără ON DELETE CASCADE, InnoDB tratează lipsa clauzei ca NO ACTION și respinge ștergerea părintelui care încă are vânzări. CREATE SCHEMA nu scrie această regulă.
        return "CREATE TABLE sales (purchase_number INT NOT NULL AUTO_INCREMENT, date_of_purchase DATE DEFAULT NULL, customer_id INT DEFAULT NULL, item_code VARCHAR(10) DEFAULT NULL, PRIMARY KEY (purchase_number), KEY customer_id (customer_id), CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE) ENGINE=InnoDB";
    // Începător: metoda cu propoziția s-a închis. sales încă nu există doar fiindcă metoda s-a terminat.
    // Producție: fără închidere, childMustHaveParent nu compilează și lecția pierde șirul cu sales_ibfk_1.
    // Expert: închiderea nu ține un lacăt pe customer după ce șirul a fost citit. Verificarea stă pe propoziția de scriere, pe InnoDB la sfârșitul ei.
    }
// Începător: lecția schemei s-a închis. customer și sales nu s-au creat din închiderea clasei.
// Producție: fără acolada clasei, RelationalSchema_Fast nu compilează și dispare șirul cu sales_ibfk_1.
// Expert: închiderea clasei nu este DROP TABLE și nu lasă foreign_key_checks schimbat. Dicționarul se schimbă doar când serverul primește propoziția.
}
