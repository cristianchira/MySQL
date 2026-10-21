package _2_SQL_Statements._3_DCL.REVOKE;

// https://dev.mysql.com/doc/refman/8.0/en/revoke.html


/*                                                   REVOKE Statement


    ○ The REVOKE statement enables system administrators

                   * to revoke

                                 - privileges and roles,

                         which can be revoked from user accounts and roles.

                   * the exact opposite of GRANT


    ○ REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost';

      REVOKE 'role1', 'role2' FROM 'user1'@'localhost', 'user2'@'localhost';

      REVOKE SELECT ON world.* FROM 'role3';


 */

/*
TOPIC:
    REVOKE SELECT ON world.* FROM 'role3' ia privilegiul; nu șterge rândurile din world și nu șterge contul.
FAMILIE:
    Security
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu mai poată citi. Datele din world rămân.
MECHANISM:
    Serverul execută REVOKE. DROP USER scoate contul. DELETE scoate rânduri de afaceri.
LOOKALIKES:
    GRANT SELECT ON world.* TO 'role3' pune dreptul înapoi. DROP USER scoate contul, nu doar SELECT.
BOOK THEORY:
    Pagina GRANT a fost deschisă; REVOKE este opusul descris în comentariul de curs, păstrat. Pagina REVOKE ca URL separat este NERULAT AICI.
BOOK EXAMPLE:
    revokeSelectFromRole3
REPO DEMO:
    src/main/java/_2_SQL_Statements/_3_DCL/REVOKE/Revoke_.java
BREAK LAB:
    deleteWorldIsNotRevoke ține DELETE FROM world.city. DELETE schimbă rânduri. REVOKE schimbă privilegiul. DROP USER scoate contul.
TRADE-OFFS:
    DROP USER când trebuie să dispară contul. DELETE când trebuie să dispară rânduri și dreptul rămâne.
ANTI-PATTERNS:
    DELETE FROM world.city ca să retragi accesul. Datele pleacă, dreptul rămâne.
STAFF QUESTIONS:
    revokeSelectFromRole3 și deleteWorldIsNotRevoke. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un stagiar nu mai citește registrul. REVOKE SELECT ON clinic.* FROM 'intern'@'localhost'. Pacienții rămân.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă REVOKE SELECT ON world.* FROM 'role3'?
   Capcană: REVOKE SELECT șterge rândurile din world.
   Cheie: revokeSelectFromRole3: role3 pierde SELECT pe world, iar rândurile din world rămân. fără REVOKE, GRANT lăsat pe loc tot permite citirea
   Demonstrație: revokeSelectFromRole3
2. Enunț: Ce rămâne în tabelă sau în sesiune după REVOKE SELECT ON world.* FROM 'role3'?
   Capcană: REVOKE este DROP USER.
   Cheie: Efectul este: role3 pierde SELECT pe world, iar rândurile din world rămân. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: revokeSelectFromRole3
3. Enunț: De ce REVOKE SELECT ON world.* FROM 'role3' nu este vecinul său?
   Capcană: DELETE FROM city retrage privilegiul.
   Cheie: GRANT SELECT ON world.* TO 'role3' pune dreptul înapoi. DROP USER scoate contul, nu doar SELECT.
   Demonstrație: revokeSelectFromRole3
4. Enunț: Cum eșuează DELETE FROM world.city?
   Capcană: REVOKE fără FROM lasă dreptul și șterge tabela.
   Cheie: deleteWorldIsNotRevoke: DELETE schimbă rânduri. REVOKE schimbă privilegiul. DROP USER scoate contul.
   Demonstrație: deleteWorldIsNotRevoke
5. Enunț: Când nu mai trimiți REVOKE SELECT ON world.* FROM 'role3'?
   Capcană: Ca să retragi accesul, golești tabela.
   Cheie: DROP USER când trebuie să dispară contul. DELETE când trebuie să dispară rânduri și dreptul rămâne.
   Demonstrație: revokeSelectFromRole3

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE dreptul trebuie să dispară și datele să rămână -> revokeSelectFromRole3 -> REVOKE SELECT ON world.* FROM 'role3' -> nu șterge contul și nu golește world
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> deleteWorldIsNotRevoke -> DELETE FROM world.city -> DELETE schimbă rânduri. REVOKE schimbă privilegiul. DROP USER scoate contul.
    3. Granița
           -> REVOKE este propoziția pe privilegii. Ștergerea unui user dintr-un ecran poate fi DROP USER, nu REVOKE.
    4. Alternativa
           -> GRANT SELECT ON world.* TO 'role3' pune dreptul înapoi. DROP USER scoate contul, nu doar SELECT.
    5. Anti-patternul
           -> DELETE FROM world.city ca să retragi accesul. Datele pleacă, dreptul rămâne.
    6. Dovada
           -> revokeSelectFromRole3 ține propoziția. deleteWorldIsNotRevoke ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> REVOKE ia privilegiul sau rolul. Nu șterge rândurile și nu este DROP USER. GRANT este opusul.
*/

public class Revoke_ {
    /*
    🟢 Începător
        Ce este            role3 pierde SELECT pe world, iar rândurile din world rămân
        De ce e nevoie     fără REVOKE, GRANT lăsat pe loc tot permite citirea
    🟡 Producție
        Cum se scrie       REVOKE SELECT ON world.* FROM 'role3'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este DROP USER 'role3'@'localhost', care scoate contul, nu doar un privilegiu
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu DROP USER 'role3'@'localhost', care scoate contul, nu doar un privilegiu.
    */
    // Începător: revokeSelectFromRole3 păstrează propoziția pentru role3 pierde SELECT pe world, iar rândurile din world rămân
    // Producție: public static String revokeSelectFromRole3(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. REVOKE nu este DELETE FROM world. Tabelele de afaceri rămân.
    public static String revokeSelectFromRole3() {
        // Începător: role3 pierde SELECT pe world, iar rândurile din world rămân
        // Producție: REVOKE SELECT ON world.* FROM 'role3'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: REVOKE nu este DELETE FROM world. Tabelele de afaceri rămân. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "REVOKE SELECT ON world.* FROM 'role3'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide revokeSelectFromRole3. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            dacă ai dreptul, dispar rânduri din city; privilegiul SELECT rămâne
        De ce e nevoie     fără textul greșit, a lua datele pare a lua dreptul
    🟡 Producție
        Cum se scrie       DELETE FROM world.city
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dacă ai dreptul, dispar rânduri din city; privilegiul SELECT rămâne
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu DROP USER 'role3'@'localhost', care scoate contul, nu doar un privilegiu.
    */
    // Începător: deleteWorldIsNotRevoke păstrează textul greșit pentru dacă ai dreptul, dispar rânduri din city; privilegiul SELECT rămâne
    // Producție: public static String deleteWorldIsNotRevoke(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DELETE schimbă rânduri. REVOKE schimbă privilegiul. DROP USER scoate contul.
    public static String deleteWorldIsNotRevoke() {
        // Începător: dacă ai dreptul, dispar rânduri din city; privilegiul SELECT rămâne
        // Producție: DELETE FROM world.city. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: DELETE schimbă rânduri. REVOKE schimbă privilegiul. DROP USER scoate contul. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "DELETE FROM world.city";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide deleteWorldIsNotRevoke. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
