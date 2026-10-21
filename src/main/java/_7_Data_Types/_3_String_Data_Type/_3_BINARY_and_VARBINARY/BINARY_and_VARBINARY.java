package _7_Data_Types._3_String_Data_Type._3_BINARY_and_VARBINARY;

// The BINARY and VARBINARY Types
//https://dev.mysql.com/doc/refman/8.0/en/binary-varbinary.html
/*
TOPIC:
    BINARY(4) ține octeți la lungime fixă; CHAR(4) ține caractere și se compară după colaționare.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu compari un token de octeți ca pe un nume. 'ab' în BINARY nu este același mecanism cu CHAR.
MECHANISM:
    Serverul stochează BINARY și VARBINARY ca șiruri de octeți. Comparația este pe octeți, nu pe colaționarea de caractere a lui CHAR.
LOOKALIKES:
    CHAR(4) completează cu spații de caracter și le poate pierde la citire. BINARY nu folosește colaționarea de text.
BOOK THEORY:
    Pagina CHAR 9.7 a fost deschisă și desparte șirurile de caractere. Pagina BINARY ca URL separat este NERULAT AICI. Comentariul de curs numește tipurile și rămâne.
BOOK EXAMPLE:
    binaryHoldsBytes
REPO DEMO:
    src/main/java/_7_Data_Types/_3_String_Data_Type/_3_BINARY_and_VARBINARY/BINARY_and_VARBINARY.java
BREAK LAB:
    charCollatesTheToken ține CREATE TABLE token (id CHAR(4)). CHAR este caracter și colaționare. BINARY este octet. VARBINARY este lungimea folosită în octeți.
TRADE-OFFS:
    VARBINARY când lungimea în octeți variază. CHAR când sunt caractere.
ANTI-PATTERNS:
    CHAR(4) pentru un token de octeți. Comparația nu mai este pe octeți.
STAFF QUESTIONS:
    binaryHoldsBytes și charCollatesTheToken. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un hash scurt fix este BINARY. Numele afișat este VARCHAR sau CHAR, nu hash-ul.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE token (id BINARY(4))?
   Capcană: BINARY(4) este CHAR(4) mai rapid.
   Cheie: binaryHoldsBytes: id ține 4 octeți, nu 4 caractere comparate ca text. fără BINARY, CHAR compară după colaționare și poate ignora spațiile la PAD SPACE
   Demonstrație: binaryHoldsBytes
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE token (id BINARY(4))?
   Capcană: BINARY se sortează după colaționarea utf8.
   Cheie: Efectul este: id ține 4 octeți, nu 4 caractere comparate ca text. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: binaryHoldsBytes
3. Enunț: De ce CREATE TABLE token (id BINARY(4)) nu este vecinul său?
   Capcană: VARBINARY completează cu spații ca CHAR.
   Cheie: CHAR(4) completează cu spații de caracter și le poate pierde la citire. BINARY nu folosește colaționarea de text.
   Demonstrație: binaryHoldsBytes
4. Enunț: Cum eșuează CREATE TABLE token (id CHAR(4))?
   Capcană: Tokenul de octeți se pune în VARCHAR ca să fie unic.
   Cheie: charCollatesTheToken: CHAR este caracter și colaționare. BINARY este octet. VARBINARY este lungimea folosită în octeți.
   Demonstrație: charCollatesTheToken
5. Enunț: Când nu mai trimiți CREATE TABLE token (id BINARY(4))?
   Capcană: Afișarea hex este tipul.
   Cheie: VARBINARY când lungimea în octeți variază. CHAR când sunt caractere.
   Demonstrație: binaryHoldsBytes

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE valoarea este octet, de lungime fixă -> binaryHoldsBytes -> CREATE TABLE token (id BINARY(4)) -> nu sortează alfabetic un nume și nu este VARCHAR
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> charCollatesTheToken -> CREATE TABLE token (id CHAR(4)) -> CHAR este caracter și colaționare. BINARY este octet. VARBINARY este lungimea folosită în octeți.
    3. Granița
           -> BINARY este tipul. Afișarea hex în client nu schimbă definiția.
    4. Alternativa
           -> CHAR(4) completează cu spații de caracter și le poate pierde la citire. BINARY nu folosește colaționarea de text.
    5. Anti-patternul
           -> CHAR(4) pentru un token de octeți. Comparația nu mai este pe octeți.
    6. Dovada
           -> binaryHoldsBytes ține propoziția. charCollatesTheToken ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> BINARY ține octeți fix. VARBINARY ține lungimea folosită în octeți. CHAR ține caractere și se compară după colaționare.
*/

public class BINARY_and_VARBINARY {
    /*
    🟢 Începător
        Ce este            id ține 4 octeți, nu 4 caractere comparate ca text
        De ce e nevoie     fără BINARY, CHAR compară după colaționare și poate ignora spațiile la PAD SPACE
    🟡 Producție
        Cum se scrie       CREATE TABLE token (id BINARY(4))
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este id CHAR(4), care este caracter
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu id CHAR(4), care este caracter.
    */
    // Începător: binaryHoldsBytes păstrează propoziția pentru id ține 4 octeți, nu 4 caractere comparate ca text
    // Producție: public static String binaryHoldsBytes(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Nu pune un nume de persoană în BINARY dacă vrei ordinea din colaționare.
    public static String binaryHoldsBytes() {
        // Începător: id ține 4 octeți, nu 4 caractere comparate ca text
        // Producție: CREATE TABLE token (id BINARY(4)). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Nu pune un nume de persoană în BINARY dacă vrei ordinea din colaționare. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE token (id BINARY(4))";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide binaryHoldsBytes. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            id se compară ca text, nu ca octeți
        De ce e nevoie     fără textul greșit, CHAR pare BINARY mai lizibil
    🟡 Producție
        Cum se scrie       CREATE TABLE token (id CHAR(4))
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care id se compară ca text, nu ca octeți
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu id CHAR(4), care este caracter.
    */
    // Începător: charCollatesTheToken păstrează textul greșit pentru id se compară ca text, nu ca octeți
    // Producție: public static String charCollatesTheToken(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CHAR este caracter și colaționare. BINARY este octet. VARBINARY este lungimea folosită în octeți.
    public static String charCollatesTheToken() {
        // Începător: id se compară ca text, nu ca octeți
        // Producție: CREATE TABLE token (id CHAR(4)). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: CHAR este caracter și colaționare. BINARY este octet. VARBINARY este lungimea folosită în octeți. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE token (id CHAR(4))";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide charCollatesTheToken. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
