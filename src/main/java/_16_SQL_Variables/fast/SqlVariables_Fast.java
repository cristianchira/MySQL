/*
TOPIC:
    Alegi domeniul: SET SESSION schimbă copia conexiunii curente. SET GLOBAL o pregătește doar pentru conexiunile noi.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied for user 'root'@'localhost' (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Modul TRADITIONAL trebuie să stea pe conexiunea deschisă, nu pe sesiunile deja conectate ale altora și nu pe global.
MECHANISM:
    SQL_VARIABLES.sessionCopyTheCurrentConnectionKeeps ține SET SESSION sql_mode = 'TRADITIONAL'. main doar o tipărește.
LOOKALIKES:
    SET GLOBAL nu rescrie @@SESSION deja copiat. SET SESSION max_connections este ERROR 1229. DECLARE și @ sunt ceilalți copii ai folderului, nu această propoziție.
BOOK THEORY:
    Manualul 9.7, 15.7.6.1, deschis în tur: SET SESSION schimbă copia până la o nouă atribuire sau până la sfârșitul sesiunii. SET GLOBAL nu atinge sesiunile curente. PDF-urile din folder nu conțin SET SESSION sql_mode = 'TRADITIONAL'. Global+Variables.pdf spune că globalul se aplică tuturor conexiunilor.
BOOK EXAMPLE:
    main
REPO DEMO:
    src/main/java/_16_SQL_Variables/SQL_VARIABLES.java
BREAK LAB:
    SQL_VARIABLES.breakSessionKeywordOnGlobalOnly ține SET SESSION max_connections = 1000. main nu o tipărește.
TRADE-OFFS:
    SET GLOBAL max_connections când limita este pentru conectările noi. DECLARE când valoarea moare la END. @ când numărul inventat trebuie să treacă de rutină, pe aceeași conexiune.
ANTI-PATTERNS:
    SET GLOBAL sql_mode ca să repari conexiunea deja deschisă.
STAFF QUESTIONS:
    Întrebările 1–50 din SqlVariables_Staff_Interview.
NOVEL TRANSFER:
    Tejgheaua deschisă a farmaciei primește TRADITIONAL doar pe conexiunea ei.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei variabile de sesiune (session system variable):
       SET SESSION sql_mode = 'TRADITIONAL' schimbă copia de pe conexiunea curentă. La deconectare copia dispare.

Exemplu:
       SqlVariables_Terms.explainSessionVariable()
       Celelalte conexiuni își păstrează modul.

Definiția unei variabile globale (global system variable):
       SET GLOBAL max_connections = 1000 schimbă valoarea copiată de conexiunile care se deschid după. Sesiunile deschise nu sunt rescrise.

Exemplu:
       SqlVariables_Terms.explainGlobalVariable()
       SET SESSION max_connections este ERROR 1229.

Definiția unei variabile de utilizator (user-defined variable):
       SET @p_emp_no = 0 trăiește pe sesiune până la deconectare și nu schimbă sql_mode.

Exemplu:
       SqlVariables_Terms.explainUserVariable()
       Alt client nu vede @p_emp_no.

Definiția unei variabile locale (local variable):
       DECLARE v_avg_salary în BEGIN...END moare la END. Nu rămâne pe conexiune.

Exemplu:
       SqlVariables_Terms.explainLocalVariable()
       SET @p_emp_no încă se vede după CALL.

Definiția unei coloane cu același nume (column):
       Dacă variabila locală poartă numele coloanei, SELECT citește variabila, nu coloana.

Exemplu:
       SqlVariables_Terms.explainColumnNameCollision()
       WHERE xname = xname compară variabila cu ea.

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE doar conexiunea asta schimbă modul -> main -> SET SESSION sql_mode = 'TRADITIONAL' tipărit -> nu garantează că serverul a primit șirul
    2. Eșecul cel mai mic
           -> DECIDE pui SESSION pe max_connections -> SQL_VARIABLES.breakSessionKeywordOnGlobalOnly -> SET SESSION max_connections = 1000 -> nu garantează ERROR 1229 în tur
    3. Granița
           -> SET SESSION este mecanismul. System.out nu execută. Click-ul din Workbench nu este sesiunea.
    4. Alternativa
           -> SET GLOBAL pentru conectările noi. DECLARE pentru END. @ pentru valoarea inventată pe sesiune.
    5. Anti-patternul
           -> SET GLOBAL sql_mode ca să schimbi copia deja luată. Copia rămâne.
    6. Dovada
           -> main tipărește sessionCopyTheCurrentConnectionKeeps. root@localhost a fost refuzat.
    7. Interviu
           -> SET SESSION schimbă copia conexiunii. SET GLOBAL nu o rescrie. SET SESSION max_connections este ERROR 1229.

🟢 Începător
    Ce este            exemplul minim: modul schimbat doar pe conexiunea curentă
    De ce e nevoie     fără șir, slide-ul despre global pare că a schimbat deja toate conexiunile
🟡 Producție
    Cum se scrie       System.out.println(SQL_VARIABLES.sessionCopyTheCurrentConnectionKeeps())
🔴 Staff
    Unde               tipărirea nu este execuția. Vecinul este SET GLOBAL. Efectul pe server rămâne NERULAT.
Pași
    1. Ia propoziția din SQL_VARIABLES.
    2. O tipărește.
    3. Nu tipărește SET SESSION max_connections.
*/
// Începător: lecția scurtă stă lângă folderul care desparte sesiunea de global, de @ și de DECLARE.
// Producție: package _16_SQL_Variables.fast. Dacă linia dispare, main nu mai este lângă SQL_VARIABLES.java.
// Expert: Pachetul nu alege domeniul. Copiii LOCAL_VARIABLES, SESSION_VARIABLES, GLOBAL_VARIABLES și USER_DEFINED_VS_SYSTEM_VARIABLES rămân.
package _16_SQL_Variables.fast;

// Începător: clasa pornește o singură propoziție, cea care schimbă modul pe conexiunea curentă.
// Producție: public final class SqlVariables_Fast. Fără ea, nu există main pentru exemplul minim.
// Expert: Nu predă fiecare copil în întregime. Nu creează un al doilea Fast.
public final class SqlVariables_Fast {
    // Începător: pornește exemplul și scrie propoziția, nu un rând într-o tabelă.
    // Producție: public static void main(String[] args). args nu este o variabilă de sesiune.
    // Expert: Nu cheamă breakSessionKeywordOnGlobalOnly. Nu deschide JDBC. Tipărirea nu ține loc de SET GLOBAL.
    public static void main(String[] args) {
        // Începător: pe ecran apare textul care ar schimba sql_mode doar pe conexiunea care îl primește.
        // Producție: System.out.println(SQL_VARIABLES.sessionCopyTheCurrentConnectionKeeps()), adică SET SESSION sql_mode = 'TRADITIONAL'. Fără linie, exemplul minim dispare.
        // Expert: println nu trimite propoziția la 3306. SET GLOBAL tot nu rescrie copia deja luată. DECLARE și @ rămân în copiii lor.
        System.out.println(_16_SQL_Variables.SQL_VARIABLES.sessionCopyTheCurrentConnectionKeeps());
        // Începător: exemplul s-a oprit după ce a arătat propoziția.
        // Producție: acolada închide main. args rămâne nefolosit.
        // Expert: Refuzul root@localhost nu este ERROR 1229. SET SESSION max_connections rămâne în cealaltă metodă.
    }
    // Începător: lecția se oprește la copia de sesiune.
    // Producție: acolada închide SqlVariables_Fast. Nu mai există o a doua clasă Fast.
    // Expert: Cine întreabă limita pentru conectările noi iese la Global_Variables. Cine întreabă @ iese la USER_DEFINED_VS_SYSTEM_VARIABLES.
}
