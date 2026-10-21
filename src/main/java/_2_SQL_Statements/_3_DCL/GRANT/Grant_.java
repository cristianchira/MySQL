package _2_SQL_Statements._3_DCL.GRANT;

// https://dev.mysql.com/doc/refman/8.0/en/grant.html


/*                                             GRANT Statement  General Overview


    ○  Example

                   * GRANT ALL
                     ON db1.* TO 'jeffrey'@'localhost';

                   * GRANT 'role1', 'role2' TO 'user1'@'localhost', 'user2'@'localhost';

                   * GRANT SELECT ON world.* TO 'role3';


    ○ The GRANT statement enables system administrators

                   * to grant

                                 - privileges

                                 - and roles,

                         which can be granted to user accounts and roles.

    ○ These syntax restrictions apply:

                   * GRANT

                                 - cannot mix granting both privileges and roles in the same statement.

                          A given GRANT statement must grant either privileges or roles.


                   * The ON clause distinguishes whether the statement grants privileges or roles:

                                  - With ON, the statement grants privileges.


                                   - Without ON, the statement grants roles.

                                   - It is permitted to assign both privileges and roles to an account,

                                                  • but you must use separate GRANT statements,

                                                   • each with syntax appropriate to what is to be granted.
 */

/*
TOPIC:
    GRANT SELECT ON db1.* TO 'jeffrey'@'localhost' dă privilegiul; fără ON, GRANT dă roluri, nu privilegii.
FAMILIE:
    Security
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu amesteci rolul cu dreptul pe tabelă. După propoziția cu ON, jeffrey are SELECT.
MECHANISM:
    Serverul se uită la ON. Cu ON sunt privilegii. Fără ON sunt roluri. O propoziție nu le dă pe amândouă.
LOOKALIKES:
    GRANT 'role1' TO 'jeffrey'@'localhost' dă rolul și nu dă SELECT pe db1.
BOOK THEORY:
    Pagina GRANT 9.7 a fost deschisă. Comentariul de curs spune deja: cu ON sunt privilegii, fără ON sunt roluri.
BOOK EXAMPLE:
    grantSelectUsesOn
REPO DEMO:
    src/main/java/_2_SQL_Statements/_3_DCL/GRANT/Grant_.java
BREAK LAB:
    grantRoleHasNoOn ține GRANT 'role1' TO 'jeffrey'@'localhost'. Fără ON, GRANT atribuie roluri. SELECT pe db1 cere ON.
TRADE-OFFS:
    GRANT de rol, fără ON, când atribui un rol deja creat.
ANTI-PATTERNS:
    GRANT 'role1' TO jeffrey ca să citească db1. Din această propoziție nu apare SELECT.
STAFF QUESTIONS:
    grantSelectUsesOn și grantRoleHasNoOn. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un rol de citire se atribuie fără ON. SELECT pe schema de probe cere ON lab.*.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă GRANT SELECT ON db1.* TO 'jeffrey'@'localhost'?
   Capcană: GRANT 'role1' TO jeffrey îi dă SELECT pe toate tabelele.
   Cheie: grantSelectUsesOn: jeffrey primește SELECT pe obiectele din db1. fără ON, aceeași formă nu mai este privilegiul pe tabelă
   Demonstrație: grantSelectUsesOn
2. Enunț: Ce rămâne în tabelă sau în sesiune după GRANT SELECT ON db1.* TO 'jeffrey'@'localhost'?
   Capcană: ON este opțional și la privilegii.
   Cheie: Efectul este: jeffrey primește SELECT pe obiectele din db1. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: grantSelectUsesOn
3. Enunț: De ce GRANT SELECT ON db1.* TO 'jeffrey'@'localhost' nu este vecinul său?
   Capcană: Poți da SELECT și un rol în aceeași propoziție.
   Cheie: GRANT 'role1' TO 'jeffrey'@'localhost' dă rolul și nu dă SELECT pe db1.
   Demonstrație: grantSelectUsesOn
4. Enunț: Cum eșuează GRANT 'role1' TO 'jeffrey'@'localhost'?
   Capcană: GRANT creează contul jeffrey.
   Cheie: grantRoleHasNoOn: Fără ON, GRANT atribuie roluri. SELECT pe db1 cere ON.
   Demonstrație: grantRoleHasNoOn
5. Enunț: Când nu mai trimiți GRANT SELECT ON db1.* TO 'jeffrey'@'localhost'?
   Capcană: Ca să dai un rol, scrii ON db1.*.
   Cheie: GRANT de rol, fără ON, când atribui un rol deja creat.
   Demonstrație: grantSelectUsesOn

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE dai un privilegiu pe schemă, nu un rol -> grantSelectUsesOn -> GRANT SELECT ON db1.* TO 'jeffrey'@'localhost' -> nu creează rolul role1 și nu creează contul
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> grantRoleHasNoOn -> GRANT 'role1' TO 'jeffrey'@'localhost' -> Fără ON, GRANT atribuie roluri. SELECT pe db1 cere ON.
    3. Granița
           -> ON este semnul din propoziție. Ecranul de privilegii nu înlocuiește clauza.
    4. Alternativa
           -> GRANT 'role1' TO 'jeffrey'@'localhost' dă rolul și nu dă SELECT pe db1.
    5. Anti-patternul
           -> GRANT 'role1' TO jeffrey ca să citească db1. Din această propoziție nu apare SELECT.
    6. Dovada
           -> grantSelectUsesOn ține propoziția. grantRoleHasNoOn ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> Cu ON, GRANT dă privilegii. Fără ON, dă roluri. Nu le amesteci într-o propoziție. Contul se face cu CREATE USER.
*/

public class Grant_ {
    /*
    🟢 Începător
        Ce este            jeffrey primește SELECT pe obiectele din db1
        De ce e nevoie     fără ON, aceeași formă nu mai este privilegiul pe tabelă
    🟡 Producție
        Cum se scrie       GRANT SELECT ON db1.* TO 'jeffrey'@'localhost'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este GRANT 'role1' TO 'jeffrey'@'localhost', care atribuie un rol
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu GRANT 'role1' TO 'jeffrey'@'localhost', care atribuie un rol.
    */
    // Începător: grantSelectUsesOn păstrează propoziția pentru jeffrey primește SELECT pe obiectele din db1
    // Producție: public static String grantSelectUsesOn(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. O propoziție care amestecă privilegiul și rolul este respinsă.
    public static String grantSelectUsesOn() {
        // Începător: jeffrey primește SELECT pe obiectele din db1
        // Producție: GRANT SELECT ON db1.* TO 'jeffrey'@'localhost'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: O propoziție care amestecă privilegiul și rolul este respinsă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "GRANT SELECT ON db1.* TO 'jeffrey'@'localhost'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide grantSelectUsesOn. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            jeffrey primește rolul, nu SELECT pe db1 din această propoziție
        De ce e nevoie     fără textul greșit, orice GRANT pare drept pe tabelă
    🟡 Producție
        Cum se scrie       GRANT 'role1' TO 'jeffrey'@'localhost'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care jeffrey primește rolul, nu SELECT pe db1 din această propoziție
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu GRANT 'role1' TO 'jeffrey'@'localhost', care atribuie un rol.
    */
    // Începător: grantRoleHasNoOn păstrează textul greșit pentru jeffrey primește rolul, nu SELECT pe db1 din această propoziție
    // Producție: public static String grantRoleHasNoOn(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Fără ON, GRANT atribuie roluri. SELECT pe db1 cere ON.
    public static String grantRoleHasNoOn() {
        // Începător: jeffrey primește rolul, nu SELECT pe db1 din această propoziție
        // Producție: GRANT 'role1' TO 'jeffrey'@'localhost'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Fără ON, GRANT atribuie roluri. SELECT pe db1 cere ON. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "GRANT 'role1' TO 'jeffrey'@'localhost'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide grantRoleHasNoOn. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
