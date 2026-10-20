package _10_JOINs;

// Martin G. Introduction to join
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8429208#questions


/*                                             JOINs


    ○ is the SQL tool that allow us to construct

                   * a relationship between objects

    ○ A join shows a result set,

                   * containing fields derived from two or more tables

    ○ We must find a related column from the two tables

                   * that contains the same type of data
                                  HISTORICAL: propoziția de deasupra nu este regula. Manualul 9.7, 15.2.13.2, deschis în tur: ON este orice search_condition, de forma unui WHERE. Tipurile nu trebuie să fie identice. Potrivirea este condiția. Comentariul de mai jos, că tabelele nu trebuie să fie adiacente în schemă, rămâne.

                   * we will be free to add columns from these two tables to our output

            The columns you use to relate tables must represent

                   * the same object such as ID,

                   * but the tables you are considering

                               - NEED NOT be logically adjacent in the relational schema.   ----------

             What is important is

                               - to find a related column connecting the two tables.


 */

/*
TOPIC:
    Alegi îmbinarea după rândul care trebuie să rămână: perechea, rândul din stânga fără pereche, produsul, sau două rezultate puse unul după altul.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Întrebarea „ce manager are departament” nu are voie să fie aceeași propoziție cu „ce manager nu are departament”. După INNER JOIN, managerul fără departments_dup lipsește din rezultat.
MECHANISM:
    Serverul execută șirul din matchedManagerAndDepartment la sfârșitul SELECT-ului. ON compară dept_no. Fără pereche, rândul din dept_manager_dup cade. Pagina JOIN nu numește un lacăt. Rezultatul nu se vede la COMMIT.
LOOKALIKES:
    LEFT JOIN păstrează managerul și pune NULL la dreapta. WHERE d.dept_no IS NULL după INNER nu găsește lipsa, pentru că INNER a scos deja rândul. Subcererea cu IN întoarce o listă de dept_no și nu este același plan. UNION pune blocuri unul după altul, nu coloane din două tabele.
BOOK THEORY:
    Manualul 9.7, 15.2.13.2, deschis în tur: INNER JOIN și virgula fără condiție dau produsul cartezian; JOIN, CROSS JOIN și INNER JOIN sunt echivalente sintactic în MySQL; LEFT JOIN fără pereche umple dreapta cu NULL; ON vede doar operanzii lui, iar virgula are precedență mai mică, de unde Unknown column în ON; USING și NATURAL coalizează coloana comună. 10.2.1.9: WHERE care este fals pe rândul NULL generat transformă LEFT JOIN în inner join. 15.2.14: UNION scoate duplicatele, ALL le păstrează, numele vin din primul bloc. 14.19.3: ONLY_FULL_GROUP_BY, implicit, respinge coloana neagregată care nu depinde de GROUP BY, cu ERROR 1055. Joins.pdf, deschis în tur, are pe primele două pagini doar titlul. Nu spune ce rând cade.
BOOK EXAMPLE:
    matchedManagerAndDepartment
REPO DEMO:
    src/main/java/_10_JOINs/JOINs.java
BREAK LAB:
    innerCannotListTheMissingDepartment pune WHERE d.dept_no IS NULL după INNER JOIN. Managerul fără departament nu apare. În tur șirul nu a fost trimis.
TRADE-OFFS:
    LEFT JOIN când managerul fără departament trebuie să rămână. UNION ALL când listezi două rezultate și copia trebuie păstrată. Subcererea când nu îți trebuie coloane din departments_dup și join-ul ar înmulți rândurile. Aplicația când codul de departament trăiește într-un sistem pe care propoziția nu îl vede.
ANTI-PATTERNS:
    INNER JOIN urmat de WHERE d.dept_no IS NULL, ca să „găsești” managerul fără departament.
STAFF QUESTIONS:
    Întrebările 1–50 din Joins_Staff_Interview. Cheia este scrisă acolo. Nu există main care să o citească.
NOVEL TRANSFER:
    Un depozit: INNER JOIN arată coletul care are raft. LEFT JOIN cu raft NULL arată coletul fără raft. CROSS JOIN este matricea colet-raft, nu așezarea de azi. UNION ALL lipește două liste de colete și păstrează coletul scris de două ori.
SPACED REVIEW:
    O zi: 21 octombrie 2026, managerul fără pereche cade la INNER. Șapte zile: 27 octombrie 2026, WHERE IS NULL după INNER nu îl aduce înapoi. Treizeci de zile: 19 noiembrie 2026, UNION nu este un join, iar subcererea nu este același plan.

Definiția unei îmbinări (join):
       Îmbinarea păstrează, din două referințe, rândurile pe care le cere forma scrisă: perechea, stânga cu NULL, sau produsul.

Exemplu:
       Joins_Terms.explainJoin()
       Managerul al cărui dept_no nu este în departments_dup nu apare după INNER JOIN.

Definiția unei referințe de tabelă (table reference):
       Referința este numele din FROM, cu alias sau fără. Manualul o numește și expresie de join. Două aliasuri pot numi aceeași tabelă stocată.

Exemplu:
       Joins_Terms.explainTableReference()
       dept_manager_dup m și departments_dup d sunt două referințe, nu o copie a rândurilor.

Definiția unei condiții de îmbinare (ON):
       ON este search_condition care spune ce pereche rămâne. Poate vedea doar operanzii acelei îmbinări. WHERE de după nu este această condiție.

Exemplu:
       Joins_Terms.explainOnCondition()
       ON m.dept_no = d.dept_no ține perechea. WHERE d.dept_no IS NULL după INNER nu găsește lipsa.

Definiția unei coloane (column):
       Coloana din ON sau din USING este valoarea comparată. USING o arată o dată în SELECT *. ON o arată din ambele referințe.

Exemplu:
       Joins_Terms.explainColumn()
       dept_no scris în USING apare o dată. Scris doar în ON apare și la manager, și la departament.

Definiția unui index (index hint):
       Indexul de pe coloana din ON poate schimba planul. Nu schimbă rândul care se potrivește. Hint-ul din gramatica JOIN spune optimizerului ce index să încerce.

Exemplu:
       Joins_Terms.explainIndex()
       Fără index pe dept_no, perechea tot este pereche. Produsul de la JOIN fără ON tot este produs.

1. Enunț: Ce problemă rezolvă alegerea dintre INNER JOIN și LEFT JOIN pe dept_manager_dup și departments_dup?
   Capcană: Orice JOIN aduce managerul și departamentul, iar lipsa se vede tot acolo, cu un rând gol.
   Cheie: matchedManagerAndDepartment păstrează doar perechea pe dept_no. Managerul fără departments_dup cade la sfârșitul SELECT-ului. Dacă întrebarea este lipsa, alegerea este LEFT JOIN, nu același INNER.
   Demonstrație: matchedManagerAndDepartment
2. Enunț: Ce se întâmplă cu rândul din dept_manager_dup al cărui dept_no nu este în departments_dup?
   Capcană: Rândul rămâne în rezultat până la COMMIT, cu dept_name gol, pentru că join-ul nu șterge din tabelă.
   Cheie: În matchedManagerAndDepartment rândul nu este în rezultat. Tabela dept_manager_dup nu pierde rândul: SELECT nu scrie. Efectul se vede la sfârșitul propoziției, nu la COMMIT. Pagina JOIN nu ține un lacăt după ce rezultatul a plecat.
   Demonstrație: matchedManagerAndDepartment
3. Enunț: De ce IN (SELECT dept_no FROM departments_dup) nu este această îmbinare?
   Capcană: Lista de departamente întoarce aceiași manageri, deci planul și înmulțirea rândurilor sunt aceleași.
   Cheie: subqueryListsManagersByDepartmentList întreabă dacă dept_no este în listă. Nu aduce dept_name în același FROM și nu este planul lui INNER JOIN. Același manager în rezultat nu înseamnă același plan.
   Demonstrație: subqueryListsManagersByDepartmentList
4. Enunț: Cum eșuează INNER JOIN când cauți managerul fără departament cu WHERE d.dept_no IS NULL?
   Capcană: IS NULL găsește lipsa, pentru că departamentul care nu se potrivește vine ca NULL.
   Cheie: innerCannotListTheMissingDepartment a scos deja rândul fără pereche. Condiția pe d.dept_no nu mai are ce rând NULL să vadă. Rezultatul nu conține managerul lipsă.
   Demonstrație: innerCannotListTheMissingDepartment
5. Enunț: Când lași INNER și când schimbi spre LEFT, UNION ALL sau aplicație?
   Capcană: UNION este join-ul mai bun, iar dacă departamentul stă în alt sistem, INNER tot îl găsește.
   Cheie: INNER când întrebarea este perechea. LEFT când managerul fără departament trebuie să apară. UNION ALL când lipești două SELECT-uri și copia rămâne. Aplicația când codul de departament nu este o tabelă pe acest server. Indexul pe dept_no nu schimbă alegerea, schimbă planul.
   Demonstrație: matchedManagerAndDepartment

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei perechea manager-departament -> matchedManagerAndDepartment -> INNER JOIN ON m.dept_no = d.dept_no -> nu garantează că departments_dup există în tur: propoziția nu a fost trimisă
    2. Eșecul cel mai mic
           -> DECIDE WHERE d.dept_no IS NULL după INNER, ca să vezi managerul fără departament -> innerCannotListTheMissingDepartment -> rezultatul nu are acel manager -> nu garantează un rând NULL, pentru că INNER l-a scos înainte
    3. Granița
           -> Potrivirea este mecanismul MySQL, la sfârșitul SELECT-ului. Șirul din Java nu este execuția. Click-ul din Workbench nu alege join-ul. Aliasul m nu creează o tabelă.
    4. Alternativa
           -> LEFT JOIN când lipsa trebuie să rămână. UNION ALL când lipești rezultate, nu coloane. Subcererea când nu îți trebuie coloana din a doua tabelă. Aplicația când codul nu este pe acest server.
    5. Anti-patternul
           -> INNER JOIN și apoi WHERE d.dept_no IS NULL. Managerul fără departament nu apare.
    6. Dovada
           -> matchedManagerAndDepartment ține INNER JOIN. innerCannotListTheMissingDepartment ține același tip cu IS NULL. root@localhost a fost refuzat cu 1045, fără parolă.
    7. Interviu
           -> INNER păstrează perechea și pierde managerul fără departament. LEFT l-ar fi păstrat, cu NULL. UNION nu este join. Efectul este în rezultat, la sfârșitul propoziției, nu în tabelă.
*/

public class JOINs {

    /*
    🟢 Începător
        Ce este            perechea manager-departament; managerul fără departament nu apare
        De ce e nevoie     fără ea, orice JOIN pare că aduce și lipsa
    🟡 Producție
        Cum se scrie       SELECT m.emp_no, d.dept_name FROM dept_manager_dup m INNER JOIN departments_dup d ON m.dept_no = d.dept_no
    🔴 Staff
        Unde               rezultatul se vede la sfârșitul SELECT-ului; LEFT ar fi păstrat managerul; subcererea nu este acest plan
    Pași
        1. Ține INNER JOIN pe dept_no.
        2. Nu trimite șirul: root@localhost a primit 1045, fără parolă.
        3. Nu pune WHERE IS NULL aici.
    */
    // Începător: metoda păstrează propoziția prin care rămân doar managerii care au departament.
    // Producție: public static String matchedManagerAndDepartment(). Dacă dispare, fast nu mai are alegerea de pereche.
    // Expert: Metoda nu deschide JDBC. Nu înlocuiește INNER cu o subcerere și nu ține lacăt după ce rezultatul ar fi plecat.
    public static String matchedManagerAndDepartment() {
        // Începător: rămân managerul și numele departamentului doar când dept_no se potrivește. Managerul fără departament nu apare.
        // Producție: INNER JOIN ON m.dept_no = d.dept_no. Dacă linia dispare, rămâne comentariul că tipurile trebuie să fie identice, iar regula citită este că ON este orice condiție.
        // Expert: INNER nu filtrează după grupare și nu este un index. Fără index pe dept_no, planul tot poate citi ambele tabele. JOIN fără cuvântul INNER ar fi aceeași pereche doar dacă ON rămâne; fără ON ar fi produsul.
        return "SELECT m.emp_no, d.dept_name FROM dept_manager_dup m INNER JOIN departments_dup d ON m.dept_no = d.dept_no";
        // Începător: metoda a înapoiat perechea, nu un rând șters din tabelă.
        // Producție: acolada închide matchedManagerAndDepartment. Șirul nu a plecat la 3306.
        // Expert: 1045 nu este un rezultat gol de la INNER. Managerul fără departament lipsește din rezultat, nu din dept_manager_dup.
    }

    /*
    🟢 Începător
        Ce este            încercarea de a găsi managerul fără departament, care nu îl găsește
        De ce e nevoie     fără ea, IS NULL pare că repară un INNER
    🟡 Producție
        Cum se scrie       același INNER JOIN, apoi WHERE d.dept_no IS NULL
    🔴 Staff
        Unde               rândul fără pereche a căzut înainte de WHERE; LEFT JOIN cu același IS NULL este celălalt copil
    Pași
        1. Ține INNER și IS NULL în aceeași propoziție.
        2. Nu o trimite.
        3. Nu o numi anti-join.
    */
    // Începător: metoda păstrează propoziția care pare că arată lipsa și nu o arată.
    // Producție: public static String innerCannotListTheMissingDepartment(). Dacă dispare, eșecul rămâne doar în comentariu.
    // Expert: Nu este LEFT JOIN. Nu este produsul de la CROSS. IS NULL pe cheia din dreapta, după INNER, nu generează rândul NULL.
    public static String innerCannotListTheMissingDepartment() {
        // Începător: managerul fără departament tot nu apare, deși condiția vorbește despre NULL.
        // Producție: INNER JOIN ON m.dept_no = d.dept_no WHERE d.dept_no IS NULL. Dacă linia dispare, nu mai ai cel mai mic eșec al alegerii greșite.
        // Expert: 10.2.1.9 spune că WHERE fals pe rândul NULL transformă LEFT în inner. Aici forma este deja inner, deci nu există rând NULL de păstrat. Anti-join-ul stă în JOIN_TRICKS, cu LEFT.
        return "SELECT m.emp_no, d.dept_name FROM dept_manager_dup m INNER JOIN departments_dup d ON m.dept_no = d.dept_no WHERE d.dept_no IS NULL";
        // Începător: metoda a păstrat propoziția periculoasă, fără să citească un rezultat.
        // Producție: acolada închide innerCannotListTheMissingDepartment. Șirul a rămas în proces.
        // Expert: Nu s-a citit un plan. Indexul pe dept_no nu face acest WHERE să vadă rândul deja scos.
    }

    /*
    🟢 Începător
        Ce este            o listă de departamente, nu coloanele departamentului lângă manager
        De ce e nevoie     fără ea, IN pare același lucru cu INNER JOIN
    🟡 Producție
        Cum se scrie       WHERE m.dept_no IN (SELECT d.dept_no FROM departments_dup d)
    🔴 Staff
        Unde               subcererea nu este îmbinarea din același FROM; același emp_no nu este același plan
    Pași
        1. Ține IN și subcererea.
        2. Nu o trimite.
        3. Nu selecta d.dept_name din afara subcererii.
    */
    // Începător: metoda păstrează întrebarea „este departamentul în listă”, nu perechea de coloane.
    // Producție: public static String subqueryListsManagersByDepartmentList(). Dacă dispare, vecinul rămâne o poveste.
    // Expert: NULL în lista lui IN schimbă testul. Această propoziție nu este UNION și nu umple un dept_name lipsă cu NULL.
    public static String subqueryListsManagersByDepartmentList() {
        // Începător: rămân managerii al căror dept_no apare în departments_dup. Numele departamentului nu este lângă ei.
        // Producție: WHERE m.dept_no IN (SELECT d.dept_no FROM departments_dup d). Dacă linia dispare, nu mai ai deosebirea de INNER JOIN.
        // Expert: Subcererea poate fi materializată sau contopită. Folderul de join nu promite planul. Un dept_no NULL în listă nu se potrivește prin =, iar IN nu aduce rândul cu coloanele din dreapta.
        return "SELECT m.emp_no FROM dept_manager_dup m WHERE m.dept_no IN (SELECT d.dept_no FROM departments_dup d)";
        // Începător: metoda s-a oprit la listă, nu la o îmbinare.
        // Producție: acolada închide subqueryListsManagersByDepartmentList. Șirul nu a plecat.
        // Expert: Folderul _6_SUBQUERIES predă subcererea până la capăt. Aici ea este doar vecinul care nu ține locul lui JOIN.
    }
    // Începător: clasa se oprește după cele trei propoziții: perechea, lipsa căutată greșit și lista din subcerere.
    // Producție: acolada închide JOINs. Nu mai există o a patra metodă care să trimită șirul la 3306.
    // Expert: Copiii _1_INNER_JOIN și _3_LEFT_JOIN rămân folderele lor. Acolada nu alege LEFT în locul lui INNER.
}
