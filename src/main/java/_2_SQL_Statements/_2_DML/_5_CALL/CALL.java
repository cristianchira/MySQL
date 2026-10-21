package _2_SQL_Statements._2_DML._5_CALL;
//CALL Statement
// https://dev.mysql.com/doc/refman/8.0/en/call.html

/*
TOPIC:
    CALL numește o procedură care există; nu este un SELECT și nu creează procedura.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să rulezi procedura deja creată. Efectul pe rânduri este cel scris în procedură, văzut când CALL se termină.
MECHANISM:
    Serverul execută CALL și intră în rutină. CREATE PROCEDURE face rutina și nu o apelează. SELECT nu sare în procedură.
LOOKALIKES:
    CREATE PROCEDURE este DDL pe rutină. O funcție intră într-o expresie și nu se apelează cu CALL.
BOOK THEORY:
    Pagina CALL nu a fost adusă separat: NERULAT AICI pentru acel URL. Comentariul de curs păstrează linkul vechi. Mecanismul folosit aici este despărțirea de CREATE PROCEDURE și de SELECT.
    HISTORICAL: pagina 15.2.1 a fost adusă după aceea. CALL invocă o procedură creată înainte cu CREATE PROCEDURE. Nu o creează. O funcție nu se pornește cu CALL. CREATE PROCEDURE doar o lasă neexecutată.
BOOK EXAMPLE:
    callListsDepartments
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_5_CALL/CALL.java
BREAK LAB:
    selectIsNotACall ține SELECT list_departments(). Procedura se apelează cu CALL. Funcția intră într-o expresie. SELECT nu ține loc de CALL pentru o procedură.
TRADE-OFFS:
    O funcție în SELECT când valoarea trebuie să stea într-o expresie, nu într-o procedură.
ANTI-PATTERNS:
    SELECT list_departments() când obiectul este o procedură. Nu pornește CALL.
STAFF QUESTIONS:
    callListsDepartments și selectIsNotACall. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Închizi tura cu CALL close_shift(). Un SELECT pe tabela de ture o citește, nu o închide.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CALL list_departments()?
   Capcană: CALL creează procedura dacă lipsește.
   Cheie: callListsDepartments: rulează procedura list_departments, iar un SELECT din afară nu intră în ea. fără CALL, CREATE PROCEDURE doar lasă rutina neexecutată
   Demonstrație: callListsDepartments
2. Enunț: Ce rămâne în tabelă sau în sesiune după CALL list_departments()?
   Capcană: SELECT list_departments() este apelul unei proceduri.
   Cheie: Efectul este: rulează procedura list_departments, iar un SELECT din afară nu intră în ea. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: callListsDepartments
3. Enunț: De ce CALL list_departments() nu este vecinul său?
   Capcană: CREATE PROCEDURE și rulează și corpul.
   Cheie: CREATE PROCEDURE este DDL pe rutină. O funcție intră într-o expresie și nu se apelează cu CALL.
   Demonstrație: callListsDepartments
4. Enunț: Cum eșuează SELECT list_departments()?
   Capcană: CALL este un SELECT cu alt nume.
   Cheie: selectIsNotACall: Procedura se apelează cu CALL. Funcția intră într-o expresie. SELECT nu ține loc de CALL pentru o procedură.
   Demonstrație: selectIsNotACall
5. Enunț: Când nu mai trimiți CALL list_departments()?
   Capcană: Ca să rulezi o procedură, o pui în WHERE.
   Cheie: O funcție în SELECT când valoarea trebuie să stea într-o expresie, nu într-o procedură.
   Demonstrație: callListsDepartments

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE rutina este o procedură și trebuie să ruleze acum -> callListsDepartments -> CALL list_departments() -> nu creează procedura și nu spune singur ce rânduri schimbă ea
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> selectIsNotACall -> SELECT list_departments() -> Procedura se apelează cu CALL. Funcția intră într-o expresie. SELECT nu ține loc de CALL pentru o procedură.
    3. Granița
           -> CALL este propoziția. Un buton de run pe un script care conține și CREATE nu este doar CALL.
    4. Alternativa
           -> CREATE PROCEDURE este DDL pe rutină. O funcție intră într-o expresie și nu se apelează cu CALL.
    5. Anti-patternul
           -> SELECT list_departments() când obiectul este o procedură. Nu pornește CALL.
    6. Dovada
           -> callListsDepartments ține propoziția. selectIsNotACall ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> CALL execută o procedură existentă. CREATE PROCEDURE nu o apelează. Funcția nu se pornește cu CALL.
*/

public class CALL {
    /*
    🟢 Începător
        Ce este            rulează procedura list_departments, iar un SELECT din afară nu intră în ea
        De ce e nevoie     fără CALL, CREATE PROCEDURE doar lasă rutina neexecutată
    🟡 Producție
        Cum se scrie       CALL list_departments()
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este SELECT dept_no FROM departments, care citește tabela și nu intră într-o procedură
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT dept_no FROM departments, care citește tabela și nu intră într-o procedură.
    */
    // Începător: callListsDepartments păstrează propoziția pentru rulează procedura list_departments, iar un SELECT din afară nu intră în ea
    // Producție: public static String callListsDepartments(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CALL pe un nume care nu există eșuează. Nu creează procedura.
    public static String callListsDepartments() {
        // Începător: rulează procedura list_departments, iar un SELECT din afară nu intră în ea
        // Producție: CALL list_departments(). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: CALL pe un nume care nu există eșuează. Nu creează procedura. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CALL list_departments()";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide callListsDepartments. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            dacă list_departments este procedură, nu funcție, serverul nu o apelează ca pe un SELECT
        De ce e nevoie     fără textul greșit, procedura pare o funcție în expresie
    🟡 Producție
        Cum se scrie       SELECT list_departments()
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dacă list_departments este procedură, nu funcție, serverul nu o apelează ca pe un SELECT
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT dept_no FROM departments, care citește tabela și nu intră într-o procedură.
    */
    // Începător: selectIsNotACall păstrează textul greșit pentru dacă list_departments este procedură, nu funcție, serverul nu o apelează ca pe un SELECT
    // Producție: public static String selectIsNotACall(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Procedura se apelează cu CALL. Funcția intră într-o expresie. SELECT nu ține loc de CALL pentru o procedură.
    public static String selectIsNotACall() {
        // Începător: dacă list_departments este procedură, nu funcție, serverul nu o apelează ca pe un SELECT
        // Producție: SELECT list_departments(). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Procedura se apelează cu CALL. Funcția intră într-o expresie. SELECT nu ține loc de CALL pentru o procedură. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT list_departments()";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide selectIsNotACall. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
