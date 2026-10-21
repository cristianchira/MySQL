package _7_Data_Types._3_String_Data_Type._2_CHAR_AND_VARCHAR;

// The CHAR and VARCHAR Types
//https://dev.mysql.com/doc/refman/8.0/en/char.html


/*                                           The CHAR and VARCHAR Types


    ○ CHAR and VARCHAR are two character data types used to store string values in a relational database.

                * They differ primarily in the way they store and handle the data.


                             Here is an explanation of the CHAR and VARCHAR types:


    1. CHAR:

                * The CHAR data type is used to store fixed-length character strings.

        When you define a CHAR column,

                * you need to specify the maximum length of the strings that can be stored in that column,

        for example, CHAR(10).

                * The length is fixed, meaning that when you store a string shorter than the defined length,

                            - it is automatically padded with trailing spaces to match the defined length.

                * Conversely, if you try to store a string longer than the defined length,

                            - it will be truncated.

         CHAR is generally more efficient when storing strings that have a consistent length.


    2. VARCHAR:

                * The VARCHAR data type is used to store variable-length character strings.

        Like CHAR,

                * you need to specify the maximum length of the strings that can be stored in a VARCHAR column,

        for example, VARCHAR(255).

                * However, unlike CHAR, VARCHAR does not pad shorter strings with trailing spaces.

                * Instead, it only uses as much storage space as the length of the actual string value

                            - plus some overhead for length information.

        VARCHAR is typically

                * more efficient when storing strings with varying lengths.

    ○ In summary,

                * use CHAR for fixed-length character strings

                * and VARCHAR for variable-length strings.

        Keep in mind that the performance difference between the two types is usually minimal,

                * so choose the type that best suits your data requirements.




Regenerate response


 */


/*
TOPIC:
    CHAR(4) ocupă lungimea declarată; VARCHAR(4) ține lungimea folosită, iar spațiile finale din CHAR dispar la citire.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu crezi că un nume scurt ocupă la fel în ambele. 'ab' în CHAR(4) se stochează pe 4, în VARCHAR pe lungimea lui plus prefix.
MECHANISM:
    Serverul completează CHAR cu spații la stocare și le scoate la citire, dacă nu este PAD_CHAR_TO_FULL_LENGTH. VARCHAR nu completează și păstrează spațiile finale.
LOOKALIKES:
    VARCHAR(4) pentru 'ab' nu este CHAR(4). BINARY este octeți, nu caractere.
BOOK THEORY:
    Manualul 9.7, 13.3.2, deschis în tur: CHAR are lungime fixă și spații la stocare; VARCHAR ține lungimea folosită. La citire, CHAR pierde spațiile finale dacă nu este PAD_CHAR_TO_FULL_LENGTH.  Comentariul de curs spune completarea cu spații la CHAR și lungimea folosită la VARCHAR. Afirmația de 50% viteză nu este în manualul deschis.
BOOK EXAMPLE:
    charPadsAb
REPO DEMO:
    src/main/java/_7_Data_Types/_3_String_Data_Type/_2_CHAR_AND_VARCHAR/CHAR_and_VARCHAR_Types.java
BREAK LAB:
    bothVarcharKeepTheTail ține CREATE TABLE vc (v VARCHAR(4), c VARCHAR(4)). CHAR pierde spațiile la citire. VARCHAR le păstrează. Două VARCHAR nu arată diferența.
TRADE-OFFS:
    VARCHAR când lungimea variază. CHAR când lungimea este fixă și accepți spațiile la stocare.
ANTI-PATTERNS:
    Să citești CHAR și să te aștepți la spațiile cu care l-ai stocat. La citire nu mai sunt, fără PAD_CHAR_TO_FULL_LENGTH.
STAFF QUESTIONS:
    charPadsAb și bothVarcharKeepTheTail. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un cod de raion fix de 4 caractere este CHAR(4). Numele raionului este VARCHAR. Spațiile din cod nu rămân la citire.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE vc (v VARCHAR(4), c CHAR(4))?
   Capcană: CHAR și VARCHAR citesc 'ab  ' la fel.
   Cheie: charPadsAb: c este fix pe 4 caractere, v este variabil, iar tabela nu are încă rândul. fără două coloane, nu vezi că același 'ab' nu se citește la fel dacă CHAR a pierdut spațiile
   Demonstrație: charPadsAb
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE vc (v VARCHAR(4), c CHAR(4))?
   Capcană: CHAR este mereu cu 50% mai rapid.
   Cheie: Efectul este: c este fix pe 4 caractere, v este variabil, iar tabela nu are încă rândul. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: charPadsAb
3. Enunț: De ce CREATE TABLE vc (v VARCHAR(4), c CHAR(4)) nu este vecinul său?
   Capcană: VARCHAR completează cu spații.
   Cheie: VARCHAR(4) pentru 'ab' nu este CHAR(4). BINARY este octeți, nu caractere.
   Demonstrație: charPadsAb
4. Enunț: Cum eșuează CREATE TABLE vc (v VARCHAR(4), c VARCHAR(4))?
   Capcană: Spațiile finale din CHAR rămân la SELECT.
   Cheie: bothVarcharKeepTheTail: CHAR pierde spațiile la citire. VARCHAR le păstrează. Două VARCHAR nu arată diferența.
   Demonstrație: bothVarcharKeepTheTail
5. Enunț: Când nu mai trimiți CREATE TABLE vc (v VARCHAR(4), c CHAR(4))?
   Capcană: CHAR(4) ține 4 octeți indiferent de setul de caractere.
   Cheie: VARCHAR când lungimea variază. CHAR când lungimea este fixă și accepți spațiile la stocare.
   Demonstrație: charPadsAb

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei o coloană fixă și una variabilă ca să vezi spațiile -> charPadsAb -> CREATE TABLE vc (v VARCHAR(4), c CHAR(4)) -> nu face CHAR cu 50% mai rapid și nu păstrează spațiile la citire
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> bothVarcharKeepTheTail -> CREATE TABLE vc (v VARCHAR(4), c VARCHAR(4)) -> CHAR pierde spațiile la citire. VARCHAR le păstrează. Două VARCHAR nu arată diferența.
    3. Granița
           -> Diferența este la stocare și la citire. Nu este o măsurătoare de viteză din curs.
    4. Alternativa
           -> VARCHAR(4) pentru 'ab' nu este CHAR(4). BINARY este octeți, nu caractere.
    5. Anti-patternul
           -> Să citești CHAR și să te aștepți la spațiile cu care l-ai stocat. La citire nu mai sunt, fără PAD_CHAR_TO_FULL_LENGTH.
    6. Dovada
           -> charPadsAb ține propoziția. bothVarcharKeepTheTail ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> CHAR este fix și pierde spațiile finale la citire. VARCHAR ține lungimea folosită și păstrează spațiile. Nu este o regulă de viteză. BINARY este octeți.
*/

public class CHAR_and_VARCHAR_Types {
    /*
    🟢 Începător
        Ce este            c este fix pe 4 caractere, v este variabil, iar tabela nu are încă rândul
        De ce e nevoie     fără două coloane, nu vezi că același 'ab' nu se citește la fel dacă CHAR a pierdut spațiile
    🟡 Producție
        Cum se scrie       CREATE TABLE vc (v VARCHAR(4), c CHAR(4))
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este CREATE TABLE vc (v VARCHAR(4), c VARCHAR(4)), care nu completează niciuna
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE TABLE vc (v VARCHAR(4), c VARCHAR(4)), care nu completează niciuna.
    */
    // Începător: charPadsAb păstrează propoziția pentru c este fix pe 4 caractere, v este variabil, iar tabela nu are încă rândul
    // Producție: public static String charPadsAb(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Nu conta pe spațiile finale din CHAR după citire. Manualul arată CONCAT pe VARCHAR care le păstrează și pe CHAR care le pierde.
    public static String charPadsAb() {
        // Începător: c este fix pe 4 caractere, v este variabil, iar tabela nu are încă rândul
        // Producție: CREATE TABLE vc (v VARCHAR(4), c CHAR(4)). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Nu conta pe spațiile finale din CHAR după citire. Manualul arată CONCAT pe VARCHAR care le păstrează și pe CHAR care le pierde. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE vc (v VARCHAR(4), c CHAR(4))";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide charPadsAb. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            ambele coloane păstrează spațiile finale, deci nu mai ai coloana care le pierde la citire
        De ce e nevoie     fără textul greșit, două VARCHAR par un CHAR și un VARCHAR
    🟡 Producție
        Cum se scrie       CREATE TABLE vc (v VARCHAR(4), c VARCHAR(4))
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care ambele coloane păstrează spațiile finale, deci nu mai ai coloana care le pierde la citire
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE TABLE vc (v VARCHAR(4), c VARCHAR(4)), care nu completează niciuna.
    */
    // Începător: bothVarcharKeepTheTail păstrează textul greșit pentru ambele coloane păstrează spațiile finale, deci nu mai ai coloana care le pierde la citire
    // Producție: public static String bothVarcharKeepTheTail(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CHAR pierde spațiile la citire. VARCHAR le păstrează. Două VARCHAR nu arată diferența.
    public static String bothVarcharKeepTheTail() {
        // Începător: ambele coloane păstrează spațiile finale, deci nu mai ai coloana care le pierde la citire
        // Producție: CREATE TABLE vc (v VARCHAR(4), c VARCHAR(4)). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: CHAR pierde spațiile la citire. VARCHAR le păstrează. Două VARCHAR nu arată diferența. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE vc (v VARCHAR(4), c VARCHAR(4))";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide bothVarcharKeepTheTail. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
