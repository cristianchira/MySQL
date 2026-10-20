package _10_JOINs.JOIN_TRICKS;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8510162#questions


/*                                    JOINs


    ○ These columns do not need to be

                  * foreign

                  * or private keys
                                  HISTORICAL: propoziția de deasupra nu este regula. Manualul 9.7, 15.2.13.2, deschis în tur, nu numește o cheie privată. ON primește orice search_condition. FOREIGN KEY nu este cerut ca rândurile să se potrivească.

                  * one should look for key columns,

                  * which are common between the tables involved in the analysis

                                - and are necessary to solve the task at hand


 */

/*
TOPIC:
    Anti-join-ul scris în manual ține managerul fără departament: LEFT JOIN pe dept_no și WHERE d.dept_no IS NULL. Aceeași condiție pusă în ON păstrează toți managerii.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Lista „manager fără departments_dup” nu are voie să conțină managerul care are departament și nu are voie să îl piardă pe cel care nu are.
MECHANISM:
    Serverul execută managerWithNoDepartment. ON caută perechea. WHERE d.dept_no IS NULL, pe o coloană care la potrivire nu este NULL, păstrează doar rândul pentru care dreapta a fost generată cu NULL. 15.2.13.2 și 10.2.1.9. Dacă dept_no din dreapta este NOT NULL, căutarea se poate opri după prima potrivire.
LOOKALIKES:
    NOT EXISTS este subcerere, nu acest LEFT. NATURAL JOIN potrivește toate coloanele cu același nume, nu doar dept_no. IS NULL mutat în ON nu taie managerul care are departament: nu se potrivește nimeni, deci toată stânga rămâne cu NULL.
BOOK THEORY:
    Manualul 9.7, 15.2.13.2, deschis în tur: WHERE right_tbl.id IS NULL după LEFT JOIN găsește stânga fără corespondent; NATURAL este INNER sau LEFT cu USING pe toate coloanele comune; USING coalizează coloana, ON o păstrează de două ori. 10.2.1.9: dacă col_name din testul IS NULL este NOT NULL, serverul se oprește după un rând care a îndeplinit ON. Tips+Tricks.pdf, deschis în tur, are titlul și cuvântul dept_name. Nu arată IS NULL.
BOOK EXAMPLE:
    managerWithNoDepartment
REPO DEMO:
    src/main/java/_10_JOINs/JOIN_TRICKS/JOIN_TRICKS_.java
BREAK LAB:
    nullTestInOnKeepsEveryManager pune d.dept_no IS NULL în ON. Șirul nu a fost trimis.
TRADE-OFFS:
    NOT EXISTS când nu îți trebuie coloane din dreapta și join-ul ți-ar înmulți stânga. NATURAL doar când lista de coloane comune este chiar cheia, și accepți că o coloană nouă cu același nume intră în potrivire. Aplicația când „fără departament” se decide în afara serverului.
ANTI-PATTERNS:
    IS NULL în ON, sau NATURAL JOIN ca să nu scrii dept_no.
STAFF QUESTIONS:
    Cele cinci de mai jos.
NOVEL TRANSFER:
    Coletul fără raft: LEFT JOIN spre raft și WHERE raft.id IS NULL. Aceeași cerere pusă în ON îți dă toate coletele, cu raftul gol.
SPACED REVIEW:
    O zi: 21 octombrie 2026, IS NULL stă în WHERE. Șapte zile: 27 octombrie 2026, în ON păstrează toată stânga. Treizeci de zile: 19 noiembrie 2026, NATURAL nu este dept_no scris de tine.

1. Enunț: Ce problemă rezolvă LEFT JOIN și WHERE d.dept_no IS NULL?
   Capcană: Orice IS NULL pe dept_no găsește managerul fără departament, indiferent unde stă condiția.
   Cheie: managerWithNoDepartment păstrează managerul pentru care dreapta nu a avut pereche. Managerul cu departament cade la WHERE, pentru că d.dept_no nu este NULL.
   Demonstrație: managerWithNoDepartment
2. Enunț: Ce rând rămâne în rezultat?
   Capcană: Amândoi cad din dept_manager_dup, iar la COMMIT se șterge managerul fără departament.
   Cheie: În rezultat rămâne managerul fără pereche. Tabela nu se schimbă. Dacă d.dept_no este NOT NULL, manualul spune că după o potrivire nu mai caută altă pereche pentru aceeași cheie. Efectul se vede la sfârșitul SELECT-ului.
   Demonstrație: managerWithNoDepartment
3. Enunț: De ce NOT EXISTS nu este acest truc?
   Capcană: Aceeași listă de manageri înseamnă același plan și aceeași înmulțire.
   Cheie: NOT EXISTS este o subcerere: întreabă dacă departments_dup întoarce un rând. Nu este LEFT JOIN și nu folosește rândul NULL generat. Coloanele din dreapta nu stau în SELECT decât dacă le aduci altfel.
   Demonstrație: managerWithNoDepartment
4. Enunț: Cum eșuează d.dept_no IS NULL pus în ON, nu în WHERE?
   Capcană: ON este locul corect al oricărui filtru de join, deci rămân doar managerii fără departament.
   Cheie: nullTestInOnKeepsEveryManager nu găsește o pereche, pentru că dept_no potrivit nu este NULL. LEFT păstrează atunci toată stânga, cu dreapta NULL. Managerul care are departament rămâne și el.
   Demonstrație: nullTestInOnKeepsEveryManager
5. Enunț: Când lași anti-join-ul și când treci la NOT EXISTS sau la NATURAL?
   Capcană: NATURAL JOIN pe dept_no este același lucru, scris mai scurt, și nu se schimbă dacă adaugi o coloană.
   Cheie: LEFT și IS NULL în WHERE când vrei lipsa și coloana din dreapta îți spune că nu a fost pereche. NOT EXISTS când nu vrei să înmulțești stânga. NATURAL când toate coloanele cu același nume sunt cheia; o coloană nouă comună intră în potrivire fără să atingi ON.
   Demonstrație: nullTestInOnKeepsEveryManager

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE managerul al cărui dept_no nu este în departments_dup -> managerWithNoDepartment -> LEFT JOIN ON m.dept_no = d.dept_no WHERE d.dept_no IS NULL -> nu garantează că dept_no din dreapta este NOT NULL: oprirea după prima potrivire cere asta
    2. Eșecul cel mai mic
           -> DECIDE IS NULL în ON -> nullTestInOnKeepsEveryManager -> fiecare manager rămâne, cu dreapta NULL -> nu garantează lista de lipsă
    3. Granița
           -> WHERE după LEFT este mecanismul anti-join-ului. Șirul Java nu caută. Workbench nu mută condiția din ON în WHERE.
    4. Alternativa
           -> NOT EXISTS când subcererea nu înmulțește. INNER când vrei perechea, nu lipsa. NATURAL doar pe o listă de nume pe care o controlezi.
    5. Anti-patternul
           -> ON m.dept_no = d.dept_no AND d.dept_no IS NULL. Managerul cu departament rămâne în rezultat.
    6. Dovada
           -> managerWithNoDepartment ține IS NULL în WHERE. nullTestInOnKeepsEveryManager îl ține în ON. 1045 a oprit trimiterea.
    7. Interviu
           -> Anti-join-ul este LEFT plus IS NULL în WHERE. În ON, aceeași cerere păstrează toată stânga. NATURAL potrivește orice coloană cu același nume.
*/

public class JOIN_TRICKS_ {

    /*
    🟢 Începător
        Ce este            managerul care nu are departament în departments_dup
        De ce e nevoie     fără WHERE IS NULL, LEFT arată și managerul care are departament
    🟡 Producție
        Cum se scrie       SELECT m.emp_no, m.dept_no FROM dept_manager_dup m LEFT JOIN departments_dup d ON m.dept_no = d.dept_no WHERE d.dept_no IS NULL
    🔴 Staff
        Unde               15.2.13.2 dă forma; 10.2.1.9 oprește căutarea după o potrivire dacă coloana din IS NULL este NOT NULL
    Pași
        1. ON doar pe egalitatea de dept_no.
        2. IS NULL în WHERE.
        3. Nu trimite șirul.
    */
    // Începător: metoda păstrează lista de manageri fără departament.
    // Producție: public static String managerWithNoDepartment(). Dacă dispare, exemplul din manual nu mai este propoziție în repo.
    // Expert: Nu deschide JDBC. Nu pune testul în ON. Nu promite că NOT EXISTS are același plan.
    public static String managerWithNoDepartment() {
        // Începător: rămâne managerul al cărui departament nu s-a găsit. Managerul cu departament nu rămâne.
        // Producție: LEFT JOIN ON m.dept_no = d.dept_no WHERE d.dept_no IS NULL. Dacă linia dispare, nu mai ai anti-join-ul.
        // Expert: INNER cu același WHERE nu vede rândul, pentru că perechea lipsă a căzut înainte. Dacă d.dept_no acceptă NULL stocat, IS NULL poate prinde și un departament cu cheie NULL, nu doar lipsa. Pagina spune oprirea după prima potrivire doar când coloana este NOT NULL.
        return "SELECT m.emp_no, m.dept_no FROM dept_manager_dup m LEFT JOIN departments_dup d ON m.dept_no = d.dept_no WHERE d.dept_no IS NULL";
        // Începător: metoda a înapoiat lipsa, nu un DELETE.
        // Producție: acolada închide managerWithNoDepartment. Șirul nu a plecat la 3306.
        // Expert: 1045 nu este o listă goală. Lista goală ar însemna că fiecare manager are departament, și doar după execuție.
    }

    /*
    🟢 Începător
        Ce este            același test de NULL, pus unde nu taie managerul cu departament
        De ce e nevoie     fără ea, ON pare locul oricărui filtru
    🟡 Producție
        Cum se scrie       LEFT JOIN ... ON m.dept_no = d.dept_no AND d.dept_no IS NULL
    🔴 Staff
        Unde               ON eșuat păstrează stânga cu NULL; WHERE ar fi scos managerul care are pereche
    Pași
        1. Mută IS NULL în ON.
        2. Nu pune WHERE.
        3. Nu o trimite.
    */
    // Începător: metoda păstrează forma care arată toți managerii, nu doar lipsa.
    // Producție: public static String nullTestInOnKeepsEveryManager(). Dacă dispare, deosebirea ON / WHERE rămâne o frază.
    // Expert: Nu este filtrul Sales din LEFT_JOIN, care respinge NULL și poate deveni inner. Aici ON-ul eșuează și LEFT păstrează stânga.
    public static String nullTestInOnKeepsEveryManager() {
        // Începător: fiecare manager rămâne, cu departamentul gol, inclusiv cel care are departament.
        // Producție: ON m.dept_no = d.dept_no AND d.dept_no IS NULL. Dacă linia dispare, nu mai ai testul mutat.
        // Expert: Un dept_no egal nu poate fi și NULL, deci nu există pereche. LEFT generează NULL la dreapta pentru toată stânga. NATURAL JOIN nu este această greșeală: el schimbă lista de coloane comune, nu locul lui IS NULL.
        return "SELECT m.emp_no, d.dept_name FROM dept_manager_dup m LEFT JOIN departments_dup d ON m.dept_no = d.dept_no AND d.dept_no IS NULL";
        // Începător: metoda a păstrat lista prea largă, fără execuție.
        // Producție: acolada închide nullTestInOnKeepsEveryManager. Șirul a rămas în proces.
        // Expert: Nu s-a citit câte rânduri ies. Numărul ar fi cel al managerilor, nu cel al lipselor, doar dacă serverul primește șirul.
    }
    // Începător: clasa se oprește la managerul fără departament și la testul pus în locul greșit.
    // Producție: acolada închide JOIN_TRICKS_. NOT EXISTS nu este una din metode.
    // Expert: NATURAL JOIN schimbă coloanele comune și nu stă în aceste două șiruri.
}
