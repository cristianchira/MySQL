package _8_Constraints.Default_Constraint;

//

/*                                                     Default Constraint

    It helps us assign a particular default value to every row of a column.

    A value different from the default can be stored in a field where the default constraint has been applied only if
       specifically indicated.


                                                   change column to default

    alter table customers
    change column number_of_complaints number_of_complaints int default 0;


                                                       Drop default

    alter table customers
    alter column number_of_complaints drop default;


 */

/*
TOPIC:
    DEFAULT 0 pe number_of_complaints umple coloana când INSERT nu o dă; DROP DEFAULT scoate umplerea, nu rândurile.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să aibă plângerile 0 când formularul nu le trimite. Un INSERT care trimite 3 păstrează 3.
MECHANISM:
    Serverul aplică DEFAULT la omitere. ALTER COLUMN ... DROP DEFAULT scoate clauza. NOT NULL nu pune 0.
LOOKALIKES:
    NOT NULL fără default, în mod strict, refuză omiterea. DEFAULT 0 o umple.
BOOK THEORY:
    Pagina INSERT 9.7 a fost deschisă: coloana omisă ia defaultul. Comentariul de curs are CHANGE și DROP DEFAULT.
BOOK EXAMPLE:
    defaultZeroComplaints
REPO DEMO:
    src/main/java/_8_Constraints/Default_Constraint/Default_Constraint.java
BREAK LAB:
    dropDefaultStopsTheFill ține ALTER TABLE customers ALTER COLUMN number_of_complaints DROP DEFAULT. DROP DEFAULT scoate umplerea pentru inserările următoare. Nu este un UPDATE pe rândurile existente.
TRADE-OFFS:
    NOT NULL fără default când omiterea trebuie să fie eroare.
ANTI-PATTERNS:
    DROP DEFAULT ca să golești plângerile existente. Rândurile vechi rămân.
STAFF QUESTIONS:
    defaultZeroComplaints și dropDefaultStopsTheFill. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Cantitatea omisă la recepție este 0 prin DEFAULT. DROP DEFAULT face ca omiterea să nu mai fie 0.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă ALTER TABLE customers ALTER COLUMN number_of_complaints SET DEFAULT 0?
   Capcană: DEFAULT 0 schimbă toate plângerile existente în 0.
   Cheie: defaultZeroComplaints: un INSERT care omite coloana lasă 0. fără DEFAULT, omiterea nu mai are 0 de pus
   Demonstrație: defaultZeroComplaints
2. Enunț: Ce rămâne în tabelă sau în sesiune după ALTER TABLE customers ALTER COLUMN number_of_complaints SET DEFAULT 0?
   Capcană: DROP DEFAULT șterge rândurile.
   Cheie: Efectul este: un INSERT care omite coloana lasă 0. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: defaultZeroComplaints
3. Enunț: De ce ALTER TABLE customers ALTER COLUMN number_of_complaints SET DEFAULT 0 nu este vecinul său?
   Capcană: NOT NULL pune 0.
   Cheie: NOT NULL fără default, în mod strict, refuză omiterea. DEFAULT 0 o umple.
   Demonstrație: defaultZeroComplaints
4. Enunț: Cum eșuează ALTER TABLE customers ALTER COLUMN number_of_complaints DROP DEFAULT?
   Capcană: INSERT trebuie să trimită 0 chiar dacă există DEFAULT.
   Cheie: dropDefaultStopsTheFill: DROP DEFAULT scoate umplerea pentru inserările următoare. Nu este un UPDATE pe rândurile existente.
   Demonstrație: dropDefaultStopsTheFill
5. Enunț: Când nu mai trimiți ALTER TABLE customers ALTER COLUMN number_of_complaints SET DEFAULT 0?
   Capcană: SET DEFAULT este un UPDATE.
   Cheie: NOT NULL fără default când omiterea trebuie să fie eroare.
   Demonstrație: defaultZeroComplaints

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE omiterea trebuie să însemne 0 -> defaultZeroComplaints -> ALTER TABLE customers ALTER COLUMN number_of_complaints SET DEFAULT 0 -> nu rescrie rândurile vechi și nu este NOT NULL
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> dropDefaultStopsTheFill -> ALTER TABLE customers ALTER COLUMN number_of_complaints DROP DEFAULT -> DROP DEFAULT scoate umplerea pentru inserările următoare. Nu este un UPDATE pe rândurile existente.
    3. Granița
           -> DEFAULT este clauza. Un 0 afișat în formular gol nu este defaultul până nu este în definiție.
    4. Alternativa
           -> NOT NULL fără default, în mod strict, refuză omiterea. DEFAULT 0 o umple.
    5. Anti-patternul
           -> DROP DEFAULT ca să golești plângerile existente. Rândurile vechi rămân.
    6. Dovada
           -> defaultZeroComplaints ține propoziția. dropDefaultStopsTheFill ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> DEFAULT umple coloana omisă. Nu schimbă rândurile vechi. DROP DEFAULT scoate umplerea. NOT NULL nu pune 0.
*/

public class Default_Constraint {
    /*
    🟢 Începător
        Ce este            un INSERT care omite coloana lasă 0
        De ce e nevoie     fără DEFAULT, omiterea nu mai are 0 de pus
    🟡 Producție
        Cum se scrie       ALTER TABLE customers ALTER COLUMN number_of_complaints SET DEFAULT 0
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este ALTER TABLE customers ALTER COLUMN number_of_complaints DROP DEFAULT
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ALTER TABLE customers ALTER COLUMN number_of_complaints DROP DEFAULT.
    */
    // Începător: defaultZeroComplaints păstrează propoziția pentru un INSERT care omite coloana lasă 0
    // Producție: public static String defaultZeroComplaints(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DEFAULT nu schimbă rândurile vechi care au deja altă valoare.
    public static String defaultZeroComplaints() {
        // Începător: un INSERT care omite coloana lasă 0
        // Producție: ALTER TABLE customers ALTER COLUMN number_of_complaints SET DEFAULT 0. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: DEFAULT nu schimbă rândurile vechi care au deja altă valoare. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE customers ALTER COLUMN number_of_complaints SET DEFAULT 0";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide defaultZeroComplaints. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            un INSERT care omite coloana nu mai primește 0 din această clauză
        De ce e nevoie     fără textul greșit, DROP DEFAULT pare că pune NULL în toate rândurile
    🟡 Producție
        Cum se scrie       ALTER TABLE customers ALTER COLUMN number_of_complaints DROP DEFAULT
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care un INSERT care omite coloana nu mai primește 0 din această clauză
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ALTER TABLE customers ALTER COLUMN number_of_complaints DROP DEFAULT.
    */
    // Începător: dropDefaultStopsTheFill păstrează textul greșit pentru un INSERT care omite coloana nu mai primește 0 din această clauză
    // Producție: public static String dropDefaultStopsTheFill(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DROP DEFAULT scoate umplerea pentru inserările următoare. Nu este un UPDATE pe rândurile existente.
    public static String dropDefaultStopsTheFill() {
        // Începător: un INSERT care omite coloana nu mai primește 0 din această clauză
        // Producție: ALTER TABLE customers ALTER COLUMN number_of_complaints DROP DEFAULT. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: DROP DEFAULT scoate umplerea pentru inserările următoare. Nu este un UPDATE pe rândurile existente. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE customers ALTER COLUMN number_of_complaints DROP DEFAULT";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide dropDefaultStopsTheFill. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
