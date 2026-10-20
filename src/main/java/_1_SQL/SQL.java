package _1_SQL;

// SQL for Data Analitics and Business Intelligence
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8337984#questions

//  Sql as a declarative language
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338030#questions


/*                                            SQL


    ○ Structured Query Language

                 * It is a programming language specifically designed for working with databases.

                 * It allows you to

                             - create,

                             - manipulate,

                             - and share data,

                     especially data from relational database management systems.

           In SQL a query,

                 * is a piece of code inducing the computer to execute(querying the database)

                             - a certain operation that will deliver the desired output.
*/

/*
TOPIC:
    SQL este propoziția pe care o execută serverul: ceri rezultatul, nu scrii pașii, iar rândul de rezultat nu devine o celulă într-o tabelă.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să primești o valoare fără să lași un rând stocat. După propoziție, tabelele rămân cum erau.
MECHANISM:
    Serverul execută șirul din statementTheServerExecutes. Rezultatul se vede la sfârșitul propoziției. Nu se deschide o tabelă, deci nu se alege un index și nu se ține un lacăt de rând.
LOOKALIKES:
    Introduction_To_DB: data_value este o celulă la întâlnirea dintre rând și coloană, chiar dacă nu trimiți nicio propoziție. Aici SELECT 1 AS data_value întoarce un rând de rezultat și nu creează celula. O buclă FOR EACH nu este propoziția: serverul o respinge înainte să înceapă.
BOOK THEORY:
    Manualul 9.7, secțiunea 1.2.1, deschisă în tur: o bază de date este o colecție structurată; serverul o adaugă, o citește și o prelucrează. SQL este limbajul standardizat cu care accesezi baza: îl tastezi, îl înglobezi sau îl ascunzi după un API. Sistemul este client/server, iar serverul execută. PDF-ul de curs deschis în tur nu desparte, pe slide-urile de început, celula stocată de propoziție. Spune că sintaxa SQL cuprinde mai multe tipuri de propoziții, că DDL definește sau modifică structuri și obiecte, că numele Structured Query Language apare la o subcerere gândită izolat, și că optimizerul alege abordări programatice diferite pentru TRUNCATE și DELETE. PDF-ul nu conține propoziția SELECT 1 AS data_value.
BOOK EXAMPLE:
    statementTheServerExecutes
REPO DEMO:
    src/main/java/_1_SQL/SQL.java
BREAK LAB:
    proseLoopTheServerRejects ține FOR EACH entity_row RETURN data_value. Nu este o propoziție. Serverul nu pornește lucrul, iar nicio celulă nu apare. În tur nu a fost trimisă: autentificarea a fost refuzată.
TRADE-OFFS:
    Rămâi în aplicație când valoarea nu trebuie văzută de alt client. Treci la _1_Introduction_To_DB când întrebarea este ce stă într-o celulă. Treci la _2_SQL_Declarative_Language când întrebarea este cine alege pașii. Treci la _2_SQL_Statements când trebuie să clasifici o propoziție care chiar schimbă schema sau rânduri.
ANTI-PATTERNS:
    Să trimiți comentariul de curs ca și cum ar fi propoziția, sau să scrii tu scanarea în locul rezultatului.
STAFF QUESTIONS:
    Întrebările 1–50 din Sql_Staff_Interview. Cheia este scrisă acolo. Nu există main care să o citească.
NOVEL TRANSFER:
    Un scaner de depozit trebuie să arate operatorului o cifră de control, fără să scrie un rând de stoc. Propoziția întoarce cifra. Raftul rămâne gol. Dacă îți trebuie cantitatea stocată, numești tabela și ieși din acest folder.
SPACED REVIEW:
    O zi: 21 octombrie 2026, rezultatul nu este o celulă. Șapte zile: 27 octombrie 2026, bucla FOR EACH nu pornește serverul. Treizeci de zile: 19 noiembrie 2026, alegi aplicația când niciun alt client nu trebuie să vadă valoarea.

1. Enunț: Ce problemă rezolvă propoziția SQL față de o tabelă goală?
   Capcană: Tabela goală întoarce deja un rând, fiindcă ai descris coloana data_value în comentariu.
   Cheie: Tabela ține celule. statementTheServerExecutes cere serverului SELECT 1 AS data_value. Apare un rând de rezultat la sfârșitul propoziției și nicio tabelă nu primește celula.
   Demonstrație: statementTheServerExecutes
2. Enunț: Ce se întâmplă cu rândurile și cu indexul când serverul termină SELECT 1 AS data_value?
   Capcană: Aliasul data_value creează o coloană, iar optimizerul pune un index ca să găsească valoarea 1.
   Cheie: Nu există tabelă în propoziție, deci nu există rând stocat, index ales sau lacăt de rând. Efectul este rândul de rezultat, vizibil la sfârșitul propoziției, nu la commit.
   Demonstrație: statementTheServerExecutes
3. Enunț: De ce propoziția nu este o celulă din Introduction_To_DB?
   Capcană: data_value este același lucru în ambele locuri: valoarea din celulă.
   Cheie: În Introduction_To_DB, data_value este conținutul de la întâlnirea unui rând cu o coloană. SELECT 1 AS data_value nu numește o tabelă. Celula lipsește și după ce clientul a primit 1.
   Demonstrație: statementTheServerExecutes
4. Enunț: Cum eșuează dacă scrii pașii în locul rezultatului?
   Capcană: FOR EACH entity_row RETURN data_value este planul pe care optimizerul îl acceptă și îl rulează.
   Cheie: proseLoopTheServerRejects nu este o propoziție SQL. Serverul nu începe lucrul. Rândurile stocate rămân neatinse. Declarativ înseamnă că tu numești rezultatul, iar optimizerul, nu textul tău, alege pașii.
   Demonstrație: proseLoopTheServerRejects
5. Enunț: Când lași valoarea în aplicație și când trimiți propoziția?
   Capcană: Orice 1 trebuie să treacă prin server, altfel nu este o valoare.
   Cheie: Dacă niciun alt client nu trebuie să vadă valoarea, o calculezi în aplicație și serverul nu primește nimic. Trimiți SELECT 1 AS data_value când rezultatul trebuie să vină de la server. Când îți trebuie o celulă stocată, nu mai ești în acest folder: numești tabela.
   Demonstrație: statementTheServerExecutes

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei o valoare de la server și nicio celulă nouă -> statementTheServerExecutes -> SELECT 1 AS data_value -> nu garantează că un client a și primit rândul, dacă propoziția nu a fost trimisă
    2. Eșecul cel mai mic
           -> DECIDE scrii tu parcurgerea -> proseLoopTheServerRejects -> FOR EACH entity_row RETURN data_value -> nu garantează un rând de eroare în tur: autentificarea a fost refuzată, iar propoziția nu a plecat
    3. Granița
           -> Propoziția este mecanismul MySQL. Șirul din Java și System.out nu sunt execuția. Comentariul de curs nu este o propoziție. Click-ul din Workbench nu este mecanismul.
    4. Alternativa
           -> Introduction_To_DB când întrebarea este celula. _2_SQL_Declarative_Language când întrebarea este cine alege pașii. Aplicația când valoarea nu trebuie să iasă din proces.
    5. Anti-patternul
           -> Comentariul „a piece of code inducing the computer” trimis la server. Serverul nu execută comentariul, iar tabela rămâne neschimbată.
    6. Dovada
           -> statementTheServerExecutes ține SELECT 1 AS data_value. proseLoopTheServerRejects ține bucla. Planul nu a fost citit: root@localhost a fost refuzat.
    7. Interviu
           -> Propoziția cere rezultatul. SELECT 1 AS data_value întoarce un rând și nu scrie o celulă. Bucla FOR EACH nu pornește serverul. Dacă îți trebuie celula, ești la tabelă, nu la această propoziție.
*/

public class SQL {

    /*
    🟢 Începător
        Ce este            propoziția care cere o valoare și nu scrie o celulă
        De ce e nevoie     fără ea, textul din comentariul de curs nu ajunge la server
    🟡 Producție
        Cum se scrie       SELECT 1 AS data_value
    🔴 Staff
        Unde               rezultatul se vede la sfârșitul propoziției; nu există index de ales și nici lacăt, pentru că nu este numită o tabelă
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu o amesteca cu bucla FOR EACH.
        3. Nu o trimite din metodă: în tur, root@localhost a fost refuzat.
    */
    // Începător: metoda păstrează propoziția pe care serverul o poate rula, fără să deschidă o tabelă.
    // Producție: public static String statementTheServerExecutes(). Dacă dispare, fast nu mai are propoziția unică.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Nu ține un lacăt, pentru că nu numește un rând stocat.
    public static String statementTheServerExecutes() {
        // Începător: clientul poate primi o valoare numită data_value, iar tabelele rămân cum erau.
        // Producție: SELECT 1 AS data_value. Fără șir, rămâne comentariul de curs, pe care serverul nu îl execută.
        // Expert: Nu este celula din Introduction_To_DB și nu este planul scris de tine. Citirea unei tabele reale stă în _2_SQL_Statements, când numești tabela.
        return "SELECT 1 AS data_value";
        // Începător: metoda a înapoiat propoziția, nu un rând scris într-o tabelă.
        // Producție: acolada închide statementTheServerExecutes. Șirul nu a fost trimis către 3306.
        // Expert: Refuzul root@localhost lasă efectul pe server NERULAT. Acolada nu este un ROLLBACK, pentru că nicio scriere nu a pornit.
    }

    /*
    🟢 Începător
        Ce este            textul care pare o parcurgere și pe care serverul nu îl poate începe
        De ce e nevoie     fără el, pare că optimizerul acceptă pașii scriși de tine
    🟡 Producție
        Cum se scrie       FOR EACH entity_row RETURN data_value
    🔴 Staff
        Unde               eșecul este înainte de orice rând citit; vecinul este propoziția declarativă, care numește rezultatul și lasă pașii optimizerului
    Pași
        1. Ține textul greșit separat de propoziția bună.
        2. Nu îl trimite în tur: autentificarea a căzut.
        3. Nu îl folosi ca plan în EXPLAIN.
    */
    // Începător: metoda păstrează cel mai mic text greșit, care nu lasă un rând în urmă.
    // Producție: public static String proseLoopTheServerRejects(). Dacă dispare, eșecul rămâne doar o poveste în comentariu.
    // Expert: Textul nu este un hint de index și nu este DELETE. Introduction_To_DB tot nu primește o celulă din el.
    public static String proseLoopTheServerRejects() {
        // Începător: niciun rând stocat nu se schimbă, pentru că serverul nu poate începe acest text.
        // Producție: FOR EACH entity_row RETURN data_value. Dacă linia dispare, nu mai ai textul greșit lângă SELECT 1 AS data_value.
        // Expert: Optimizerul din slide-ul TRUNCATE versus DELETE alege el abordarea. Acest text îi dictează pașii și de aceea nu este propoziția declarativă.
        return "FOR EACH entity_row RETURN data_value";
        // Începător: metoda s-a oprit la textul greșit, fără un rezultat.
        // Producție: acolada închide proseLoopTheServerRejects. Textul a rămas în proces.
        // Expert: Nu compara acest eșec cu un WHERE gol din _2_SQL_Statements. Aici serverul nu are o propoziție pe care să o termine.
    }
}
