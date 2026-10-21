package _2_SQL_Statements._1_DDL._1_CREATE._2_Using_Database_And_Table;


// Martin G.class Using database and table
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8339062#questions

/*                            Using Databases and tables


    ○ Whenever you would like to refer to an SQL object in your queries,
          you must specify the database to which it is applied

                1) set a default database

                            - USE database_name;

                2) call a table from a certain database

                            - database_object . sql_object



 */


/*
TOPIC:
    USE schimbă schema implicită a sesiunii; rândurile nu se mută, iar propoziția următoare caută tabela în schema aleasă.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu mai scrii schema la fiecare tabelă. După USE, schema curentă este alta; tabelele rămân unde erau.
MECHANISM:
    Serverul execută USE la sfârșitul propoziției și schimbă schema implicită a conexiunii. SELECT DATABASE() o arată abia la propoziția următoare.
LOOKALIKES:
    nume calificat sales.purchase citește tabela fără să schimbe schema sesiunii. CREATE DATABASE face schema, nu o alege.
BOOK THEORY:
    Pagina USE din manualul 9.7 nu a fost adusă separat: NERULAT AICI pentru acel URL. PDF-ul Using+Databases+and+Tables.pdf din folder a fost deschis. Efectul folosit aici este cel deja scris corect în comentariul de curs: ori USE, ori numele calificat.
    HISTORICAL: pagina 15.8.4 a fost adusă după aceea. USE face schema numită implicită pentru propozițiile următoare, până la sfârșitul sesiunii sau până la alt USE. Numele calificat tot poate citi o tabelă din altă schemă. USE nu creează schema.
BOOK EXAMPLE:
    useSetsTheSessionSchema
REPO DEMO:
    src/main/java/_2_SQL_Statements/_1_DDL/_1_CREATE/_2_Using_Database_And_Table/Using_Database_And_Table.java
BREAK LAB:
    useDoesNotCreateTheSchema ține USE missing_sales. Schema missing_sales nu apare. Rândurile din alte scheme rămân. Nu s-a făcut CREATE DATABASE.
TRADE-OFFS:
    sales.purchase când o singură propoziție trebuie să iasă din schema curentă și sesiunea trebuie să rămână unde era.
ANTI-PATTERNS:
    USE missing_sales ca să apară schema. Serverul nu o creează.
STAFF QUESTIONS:
    useSetsTheSessionSchema și useDoesNotCreateTheSchema. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O sesiune de inventar pe un depozit trebuie să citească bin, nu catalogul de produse. USE warehouse face bin implicit. Numele catalog.sku lasă sesiunea pe warehouse.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă USE sales?
   Capcană: USE copiază tabelele în schema nouă ca să le găsești mai ușor.
   Cheie: useSetsTheSessionSchema schimbă doar schema implicită a conexiunii. Rândurile rămân în tabela lor.
   Demonstrație: useSetsTheSessionSchema
2. Enunț: Când se vede schema nouă, pe rânduri sau pe propoziția următoare?
   Capcană: Rândurile se mută la sfârșitul lui USE.
   Cheie: La sfârșitul lui USE se schimbă sesiunea. SELECT DATABASE() de după o arată. Niciun rând nu se mută.
   Demonstrație: useSetsTheSessionSchema
3. Enunț: De ce nu este același lucru cu sales.purchase?
   Capcană: Prefixul sales. schimbă și el schema sesiunii până la deconectare.
   Cheie: Numele calificat alege schema pentru acea propoziție. USE o alege pentru propozițiile următoare. CREATE DATABASE este cel care face schema.
   Demonstrație: useSetsTheSessionSchema
4. Enunț: Cum eșuează USE missing_sales?
   Capcană: Serverul creează missing_sales goală și o face curentă.
   Cheie: useDoesNotCreateTheSchema nu găsește schema. Schema curentă rămâne. Nu apare niciun obiect nou.
   Demonstrație: useDoesNotCreateTheSchema
5. Enunț: Când folosești USE și când numele calificat?
   Capcană: Trebuie mereu USE, altfel serverul nu găsește tabela.
   Cheie: USE când mai multe propoziții stau în aceeași schemă. Numele calificat când o propoziție iese din schema sesiunii și nu vrei să o muți.
   Demonstrație: useSetsTheSessionSchema

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei ca propozițiile următoare să caute tabelele în sales -> useSetsTheSessionSchema -> USE sales -> nu garantează că sales există și nu mută rânduri
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> useDoesNotCreateTheSchema -> USE missing_sales -> Schema missing_sales nu apare. Rândurile din alte scheme rămân. Nu s-a făcut CREATE DATABASE.
    3. Granița
           -> USE este propoziția pe sesiune. Alegerea din arborele Workbench este click, nu schimbarea schemei.
    4. Alternativa
           -> nume calificat sales.purchase citește tabela fără să schimbe schema sesiunii. CREATE DATABASE face schema, nu o alege.
    5. Anti-patternul
           -> USE missing_sales ca să apară schema. Serverul nu o creează.
    6. Dovada
           -> useSetsTheSessionSchema ține propoziția. useDoesNotCreateTheSchema ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> USE sales schimbă schema sesiunii și nu mută rânduri. Numele calificat nu schimbă sesiunea. USE pe o schemă lipsă nu o creează.
*/

public class Using_Database_And_Table {
    /*
    🟢 Începător
        Ce este            sesiunea are schema implicită sales, iar rândurile nu se copiază
        De ce e nevoie     fără USE și fără nume calificat, SELECT * FROM purchase nu știe în ce schemă este purchase
    🟡 Producție
        Cum se scrie       USE sales
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este sales.purchase, care numește schema pe o propoziție și lasă schema sesiunii neschimbată
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu sales.purchase, care numește schema pe o propoziție și lasă schema sesiunii neschimbată.
    */
    // Începător: useSetsTheSessionSchema păstrează propoziția pentru sesiunea are schema implicită sales, iar rândurile nu se copiază
    // Producție: public static String useSetsTheSessionSchema(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. USE nu creează schema și nu ține lacăt. Dacă sales nu există, propoziția eșuează și schema curentă rămâne cea veche.
    public static String useSetsTheSessionSchema() {
        // Începător: sesiunea are schema implicită sales, iar rândurile nu se copiază
        // Producție: USE sales. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: USE nu creează schema și nu ține lacăt. Dacă sales nu există, propoziția eșuează și schema curentă rămâne cea veche. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "USE sales";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide useSetsTheSessionSchema. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul nu găsește schema și nu schimbă schema curentă
        De ce e nevoie     fără textul greșit, pare că USE creează schema pe care o numești
    🟡 Producție
        Cum se scrie       USE missing_sales
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul nu găsește schema și nu schimbă schema curentă
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu sales.purchase, care numește schema pe o propoziție și lasă schema sesiunii neschimbată.
    */
    // Începător: useDoesNotCreateTheSchema păstrează textul greșit pentru serverul nu găsește schema și nu schimbă schema curentă
    // Producție: public static String useDoesNotCreateTheSchema(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Schema missing_sales nu apare. Rândurile din alte scheme rămân. Nu s-a făcut CREATE DATABASE.
    public static String useDoesNotCreateTheSchema() {
        // Începător: serverul nu găsește schema și nu schimbă schema curentă
        // Producție: USE missing_sales. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Schema missing_sales nu apare. Rândurile din alte scheme rămân. Nu s-a făcut CREATE DATABASE. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "USE missing_sales";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide useDoesNotCreateTheSchema. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
