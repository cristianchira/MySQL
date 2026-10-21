package _7_Data_Types._3_String_Data_Type._1_String_Data_Type_Syntax;

//https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html

/*
TOPIC:
    Sintaxa tipului de șir stă în CREATE TABLE: VARCHAR(40) limitează lungimea, nu este un șir deja stocat.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să declari coloana înainte de rând. Fără lungime, VARCHAR nu este complet.
MECHANISM:
    Serverul ia tipul din definiție. Un literal 'James' într-un INSERT umple rândul doar după ce coloana există.
LOOKALIKES:
    CHAR(40) are lungime fixă. VARCHAR(40) are lungime variabilă.
BOOK THEORY:
    Manualul 9.7, 13.3.2, deschis în tur: CHAR are lungime fixă și spații la stocare; VARCHAR ține lungimea folosită. La citire, CHAR pierde spațiile finale dacă nu este PAD_CHAR_TO_FULL_LENGTH.  Fișierul nu are PDF propriu în extras. Pagina CHAR și VARCHAR 9.7 a fost deschisă.
BOOK EXAMPLE:
    declareVarcharForty
REPO DEMO:
    src/main/java/_7_Data_Types/_3_String_Data_Type/_1_String_Data_Type_Syntax/String_Data_Type_Syntax.java
BREAK LAB:
    charPadsTheName ține CREATE TABLE person (name CHAR(40)). CHAR este fix. VARCHAR este variabil. Niciunul nu este rândul.
TRADE-OFFS:
    CHAR când lungimea este fixă și accepți spațiile la stocare.
ANTI-PATTERNS:
    CHAR(40) când voiai lungimea folosită. Stocarea se completează.
STAFF QUESTIONS:
    declareVarcharForty și charPadsTheName. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Numele pacientului este VARCHAR(40). Un cod de exact 4 caractere poate fi CHAR(4).
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE person (name VARCHAR(40))?
   Capcană: VARCHAR(40) inserează un nume de 40 de caractere.
   Cheie: declareVarcharForty: name poate ține până la 40 de caractere, iar tabela încă nu are rânduri. fără lungime, definiția de șir nu spune cât încape
   Demonstrație: declareVarcharForty
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE person (name VARCHAR(40))?
   Capcană: CHAR și VARCHAR stochează la fel un nume scurt.
   Cheie: Efectul este: name poate ține până la 40 de caractere, iar tabela încă nu are rânduri. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: declareVarcharForty
3. Enunț: De ce CREATE TABLE person (name VARCHAR(40)) nu este vecinul său?
   Capcană: Fără lungime, VARCHAR ține orice.
   Cheie: CHAR(40) are lungime fixă. VARCHAR(40) are lungime variabilă.
   Demonstrație: declareVarcharForty
4. Enunț: Cum eșuează CREATE TABLE person (name CHAR(40))?
   Capcană: Sintaxa din manual creează tabela singură.
   Cheie: charPadsTheName: CHAR este fix. VARCHAR este variabil. Niciunul nu este rândul.
   Demonstrație: charPadsTheName
5. Enunț: Când nu mai trimiți CREATE TABLE person (name VARCHAR(40))?
   Capcană: 'James' este tipul.
   Cheie: CHAR când lungimea este fixă și accepți spațiile la stocare.
   Demonstrație: declareVarcharForty

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE lungimea variază și nu vrei spații de completare -> declareVarcharForty -> CREATE TABLE person (name VARCHAR(40)) -> nu taie un nume mai lung fără să te uiți la modul strict
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> charPadsTheName -> CREATE TABLE person (name CHAR(40)) -> CHAR este fix. VARCHAR este variabil. Niciunul nu este rândul.
    3. Granița
           -> Sintaxa este în CREATE TABLE. Un exemplu 'James' din PDF nu creează coloana.
    4. Alternativa
           -> CHAR(40) are lungime fixă. VARCHAR(40) are lungime variabilă.
    5. Anti-patternul
           -> CHAR(40) când voiai lungimea folosită. Stocarea se completează.
    6. Dovada
           -> declareVarcharForty ține propoziția. charPadsTheName ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> VARCHAR ține lungimea folosită. CHAR este fix și se completează la stocare. Definiția nu este un rând. Lungimea face parte din tip.
*/

public class String_Data_Type_Syntax {
    /*
    🟢 Începător
        Ce este            name poate ține până la 40 de caractere, iar tabela încă nu are rânduri
        De ce e nevoie     fără lungime, definiția de șir nu spune cât încape
    🟡 Producție
        Cum se scrie       CREATE TABLE person (name VARCHAR(40))
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este name CHAR(40), care completează cu spații la stocare
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu name CHAR(40), care completează cu spații la stocare.
    */
    // Începător: declareVarcharForty păstrează propoziția pentru name poate ține până la 40 de caractere, iar tabela încă nu are rânduri
    // Producție: public static String declareVarcharForty(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. VARCHAR(40) nu inserează 'James'. INSERT o face.
    public static String declareVarcharForty() {
        // Începător: name poate ține până la 40 de caractere, iar tabela încă nu are rânduri
        // Producție: CREATE TABLE person (name VARCHAR(40)). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: VARCHAR(40) nu inserează 'James'. INSERT o face. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE person (name VARCHAR(40))";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide declareVarcharForty. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            un nume scurt este completat cu spații la stocare, nu ținut la lungimea folosită
        De ce e nevoie     fără textul greșit, CHAR și VARCHAR par același șir
    🟡 Producție
        Cum se scrie       CREATE TABLE person (name CHAR(40))
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care un nume scurt este completat cu spații la stocare, nu ținut la lungimea folosită
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu name CHAR(40), care completează cu spații la stocare.
    */
    // Începător: charPadsTheName păstrează textul greșit pentru un nume scurt este completat cu spații la stocare, nu ținut la lungimea folosită
    // Producție: public static String charPadsTheName(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CHAR este fix. VARCHAR este variabil. Niciunul nu este rândul.
    public static String charPadsTheName() {
        // Începător: un nume scurt este completat cu spații la stocare, nu ținut la lungimea folosită
        // Producție: CREATE TABLE person (name CHAR(40)). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: CHAR este fix. VARCHAR este variabil. Niciunul nu este rândul. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE person (name CHAR(40))";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide charPadsTheName. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
