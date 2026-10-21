package _7_Data_Types._3_String_Data_Type._6_SET_TYPE;

// The SET Type
//https://dev.mysql.com/doc/refman/8.0/en/set.html


/*                                            The SET Type


    ○ A SET is a string object that can have zero or more values,

                * each of which must be chosen from a list of permitted values specified when the table is created.

    ○ Example:

                * CREATE TABLE example_table_set (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  name VARCHAR(50),
                  skills SET('Programming', 'Web Design', 'Database Management', 'Networking', 'Security')
                  ) ENGINE = InnoDB;

                  INSERT INTO example_table_set (name, skills)
                  VALUES ('John Doe', 'Programming,Database Management');


);


 */

/*
TOPIC:
    SET('Programming','Web Design','Database Management') ține zero sau mai multe valori din listă în aceeași coloană; ENUM ține una.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să stea mai multe competențe într-o coloană închisă. 'Programming,Database Management' este o valoare, nu două rânduri.
MECHANISM:
    Serverul stochează combinația din membrii declarați. O valoare care nu este în listă nu este o competență acceptată în mod strict. ENUM ar fi ținut una.
LOOKALIKES:
    ENUM('Programming','Web Design') ține o singură valoare. O tabelă copil de competențe este forma normală, nu SET.
BOOK THEORY:
    Pagina SET nu a fost adusă separat: NERULAT AICI pentru acel URL. Pagina ENUM 9.7 a fost deschisă și desparte un membru de o listă. Comentariul de curs arată SET cu mai multe valori separate prin virgulă.
    HISTORICAL: pagina 13.3.7 a fost adusă după aceea. SET ține zero sau mai multe valori din listă, despărțite prin virgulă, cel mult 64 de membri. La citire, fiecare membru apare o dată, în ordinea din definiție. În mod strict, un membru din afara listei este eroare. ENUM ține unul.
BOOK EXAMPLE:
    setKeepsSeveralSkills
REPO DEMO:
    src/main/java/_7_Data_Types/_3_String_Data_Type/_6_SET_TYPE/SET_Type.java
BREAK LAB:
    enumKeepsOneSkill ține CREATE TABLE person (name VARCHAR(50), skills ENUM('Programming', 'Web Design', 'Database Management')). ENUM ține un membru. SET ține o combinație din listă. Tabela copil ține câte un rând pe competență.
TRADE-OFFS:
    O tabelă person_skill când fiecare competență este un rând și poate avea nivel.
ANTI-PATTERNS:
    ENUM când voiai două competențe pe același rând. Rămâne una.
STAFF QUESTIONS:
    setKeepsSeveralSkills și enumKeepsOneSkill. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Alergiile unui pacient dintr-o listă scurtă pot fi SET. Dacă fiecare alergie are severitate, este tabelă copil, nu SET.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE person (name VARCHAR(50), skills SET('Programming', 'Web Design', 'Database Management'))?
   Capcană: ENUM ține 'Programming,Database Management' ca două valori.
   Cheie: setKeepsSeveralSkills: skills poate ține mai multe valori din listă pe același rând. fără SET, ENUM ar ține o singură competență
   Demonstrație: setKeepsSeveralSkills
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE person (name VARCHAR(50), skills SET('Programming', 'Web Design', 'Database Management'))?
   Capcană: SET creează câte un rând pe competență.
   Cheie: Efectul este: skills poate ține mai multe valori din listă pe același rând. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: setKeepsSeveralSkills
3. Enunț: De ce CREATE TABLE person (name VARCHAR(50), skills SET('Programming', 'Web Design', 'Database Management')) nu este vecinul său?
   Capcană: Virgula din SET este un INSERT nou.
   Cheie: ENUM('Programming','Web Design') ține o singură valoare. O tabelă copil de competențe este forma normală, nu SET.
   Demonstrație: setKeepsSeveralSkills
4. Enunț: Cum eșuează CREATE TABLE person (name VARCHAR(50), skills ENUM('Programming', 'Web Design', 'Database Management'))?
   Capcană: SET și VARCHAR cu virgule sunt același tip.
   Cheie: enumKeepsOneSkill: ENUM ține un membru. SET ține o combinație din listă. Tabela copil ține câte un rând pe competență.
   Demonstrație: enumKeepsOneSkill
5. Enunț: Când nu mai trimiți CREATE TABLE person (name VARCHAR(50), skills SET('Programming', 'Web Design', 'Database Management'))?
   Capcană: O tabelă copil este obligatorie și pentru două valori fixe.
   Cheie: O tabelă person_skill când fiecare competență este un rând și poate avea nivel.
   Demonstrație: setKeepsSeveralSkills

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei mai multe valori dintr-o listă scurtă, pe același rând -> setKeepsSeveralSkills -> CREATE TABLE person (name VARCHAR(50), skills SET('Programming', 'Web Design', 'Database Management')) -> nu înlocuiește o tabelă de competențe când lista crește sau când fiecare competență are atribute
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> enumKeepsOneSkill -> CREATE TABLE person (name VARCHAR(50), skills ENUM('Programming', 'Web Design', 'Database Management')) -> ENUM ține un membru. SET ține o combinație din listă. Tabela copil ține câte un rând pe competență.
    3. Granița
           -> SET este tipul. O bifare multiplă din formular nu este coloana până nu ajunge în definiție.
    4. Alternativa
           -> ENUM('Programming','Web Design') ține o singură valoare. O tabelă copil de competențe este forma normală, nu SET.
    5. Anti-patternul
           -> ENUM când voiai două competențe pe același rând. Rămâne una.
    6. Dovada
           -> setKeepsSeveralSkills ține propoziția. enumKeepsOneSkill ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> SET ține zero sau mai multe valori din listă, pe un rând. ENUM ține una. Virgula este separatorul, nu un rând nou. Lista lungă sau cu atribute stă într-o tabelă copil.
*/

public class SET_Type {
    /*
    🟢 Începător
        Ce este            skills poate ține mai multe valori din listă pe același rând
        De ce e nevoie     fără SET, ENUM ar ține o singură competență
    🟡 Producție
        Cum se scrie       CREATE TABLE person (name VARCHAR(50), skills SET('Programming', 'Web Design', 'Database Management'))
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este skills ENUM('Programming', 'Web Design', 'Database Management')
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu skills ENUM('Programming', 'Web Design', 'Database Management').
    */
    // Începător: setKeepsSeveralSkills păstrează propoziția pentru skills poate ține mai multe valori din listă pe același rând
    // Producție: public static String setKeepsSeveralSkills(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Virgula din valoare este separatorul SET, nu un rând nou. O competență din afara listei nu intră în mod strict.
    public static String setKeepsSeveralSkills() {
        // Începător: skills poate ține mai multe valori din listă pe același rând
        // Producție: CREATE TABLE person (name VARCHAR(50), skills SET('Programming', 'Web Design', 'Database Management')). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Virgula din valoare este separatorul SET, nu un rând nou. O competență din afara listei nu intră în mod strict. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE person (name VARCHAR(50), skills SET('Programming', 'Web Design', 'Database Management'))";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide setKeepsSeveralSkills. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            coloana ține o singură competență, iar 'Programming,Database Management' nu este combinația SET
        De ce e nevoie     fără textul greșit, ENUM pare SET
    🟡 Producție
        Cum se scrie       CREATE TABLE person (name VARCHAR(50), skills ENUM('Programming', 'Web Design', 'Database Management'))
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care coloana ține o singură competență, iar 'Programming,Database Management' nu este combinația SET
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu skills ENUM('Programming', 'Web Design', 'Database Management').
    */
    // Începător: enumKeepsOneSkill păstrează textul greșit pentru coloana ține o singură competență, iar 'Programming,Database Management' nu este combinația SET
    // Producție: public static String enumKeepsOneSkill(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. ENUM ține un membru. SET ține o combinație din listă. Tabela copil ține câte un rând pe competență.
    public static String enumKeepsOneSkill() {
        // Începător: coloana ține o singură competență, iar 'Programming,Database Management' nu este combinația SET
        // Producție: CREATE TABLE person (name VARCHAR(50), skills ENUM('Programming', 'Web Design', 'Database Management')). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: ENUM ține un membru. SET ține o combinație din listă. Tabela copil ține câte un rând pe competență. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE person (name VARCHAR(50), skills ENUM('Programming', 'Web Design', 'Database Management'))";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide enumKeepsOneSkill. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
