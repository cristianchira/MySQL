package _1_SQL._2_SQL_Declarative_Language;

//  Sql as a declarative language
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338030#questions


/*                             SQL Declarative Language


    ○ SQL is a declarative programming language

                * This means while coding you will not be interested in

                            - how you want the job done.

                 * The focus is on

                             - what results you want to obtain.

          The algorithms are built-in, and there is an optimizer,

                  * which will separate your task into smaller steps

                  * and do the magic to bring the desired output.

           When using SQL,

                  * you should concentrate on what you want to retrieve from your databases.

    ○ SQL is the programming language

                * you need to execute commands that let you

                            - create and manipulate a relational database.


                            Main components of SQL's syntax

    1.Data Definition Language (known as DDL),

    2.Data Manipulation Language (abbreviated DML),

    3. Data Control Language (DCL),

    4.Transaction Control Language (TCL).

 */

/*
TOPIC:
    Propoziția declarativă numește rezultatul. Optimizerul construiește planul. Tu nu scrii pașii.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI. EXPLAIN nu a fost citit.
INTENT:
    Să ceri data_value din entity_row fără să scrii parcurgerea. După SELECT, celula rămâne. Rezultatul pleacă la client.
MECHANISM:
    Serverul execută SELECT data_value FROM entity_row. Manualul 10.2.1 spune că optimizerul construiește planul. EXPLAIN arată indexul, dacă propoziția ajunge la server. În tur nu a ajuns.
LOOKALIKES:
    FOR EACH entity_row RETURN data_value scrie pașii și nu este propoziția. Celula din Introduction_To_DB există și fără această cerere. Clasificarea DDL, DML, DCL și TCL stă în _2_SQL_Statements.
BOOK THEORY:
    Manualul 9.7, 10.2.1, deschisă în tur: SELECT face căutarea. ANALYZE TABLE ține statisticile din care optimizerul construiește planul. EXPLAIN arată ce index este folosit. Nu transforma tu interogarea în pașii pe care optimizerul îi face deja. PDF-ul de curs, deschis în tur, pe slide-urile de început este The SQL DELETE Statement și SQL Theory. Mai departe spune că sintaxa cuprinde mai multe tipuri de propoziții și că optimizerul alege abordări diferite pentru TRUNCATE și DELETE. Nu conține SELECT data_value FROM entity_row.
BOOK EXAMPLE:
    namedResult
REPO DEMO:
    src/main/java/_1_SQL/_2_SQL_Declarative_Language/SQL_Declarative_Language.java
BREAK LAB:
    proseLoopTheServerRejects ține FOR EACH entity_row RETURN data_value. Serverul nu începe pașii scriși de tine.
TRADE-OFFS:
    Rămâi în aplicație când parcurgi o listă care nu este în tabelă. Treci la Introduction_To_DB când întrebarea este celula. Treci la _2_SQL_Statements când trebuie să spui dacă textul schimbă schema sau rândurile.
ANTI-PATTERNS:
    Să scrii parcurgerea în propoziție, sau să trimiți comentariul despre optimizer ca și cum ar fi planul.
STAFF QUESTIONS:
    Întrebările 1–50 din SqlDeclarativeLanguage_Staff_Interview. Cheia este scrisă. Nu există main de recall.
NOVEL TRANSFER:
    Un dispecer cere „cantitatea produsului 7”. Nu scrie traseul prin depozit. Serverul alege pașii. Cantitatea stocată rămâne celula, nu cererea.
SPACED REVIEW:
    O zi: 21 octombrie 2026, tu numești rezultatul. Șapte zile: 27 octombrie 2026, bucla FOR EACH nu pornește. Treizeci de zile: 19 noiembrie 2026, EXPLAIN arată planul și nu schimbă celula.

1. Enunț: Ce problemă rezolvă propoziția declarativă față de pașii scriși de tine?
   Capcană: Fără pașii tăi, optimizerul nu știe ce vrei.
   Cheie: Tu numești rezultatul: SELECT data_value FROM entity_row. Optimizerul construiește planul. FOR EACH entity_row RETURN data_value nu este propoziția.
   Demonstrație: namedResult
2. Enunț: Ce se întâmplă cu celula și cu planul când SELECT data_value FROM entity_row se termină?
   Capcană: SELECT mută data_value din tabelă în client, deci celula dispare.
   Cheie: Celula rămâne. Clientul primește un rând de rezultat la sfârșitul propoziției. Planul este al optimizerului. În tur EXPLAIN nu a fost citit.
   Demonstrație: namedResult
3. Enunț: De ce cererea nu este celula din Introduction_To_DB?
   Capcană: SELECT data_value este valoarea, deci este celula.
   Cheie: Celula este conținutul stocat și există și fără să o ceri. SELECT o citește. Dacă tabela e goală, rezultatul nu inventează un record.
   Demonstrație: namedResult
4. Enunț: Cum eșuează dacă scrii pașii?
   Capcană: FOR EACH entity_row RETURN data_value este planul pe care optimizerul îl rulează.
   Cheie: proseLoopTheServerRejects nu este o propoziție. Serverul nu începe. Celulele rămân neatinse. În tur textul nu a fost trimis.
   Demonstrație: proseLoopTheServerRejects
5. Enunț: Când lași parcurgerea în aplicație și când trimiți SELECT-ul?
   Capcană: Orice citire trebuie să fie SQL, altfel optimizerul nu există.
   Cheie: Lista care nu stă într-o tabelă se parcurge în aplicație. SELECT data_value FROM entity_row pleacă când rezultatul trebuie să vină din celulele serverului.
   Demonstrație: namedResult

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei valoarea stocată, fără să scrii pașii -> namedResult -> SELECT data_value FROM entity_row -> nu garantează un index anume: EXPLAIN nu a fost citit
    2. Eșecul cel mai mic
           -> DECIDE scrii tu parcurgerea -> proseLoopTheServerRejects -> FOR EACH entity_row RETURN data_value -> nu garantează mesajul de eroare în tur: propoziția nu a plecat
    3. Granița
           -> Propoziția și planul optimizerului sunt mecanismul. Comentariul despre optimizer și System.out nu sunt planul.
    4. Alternativa
           -> Introduction_To_DB când întrebarea este celula. Aplicația când lista nu este în tabelă.
    5. Anti-patternul
           -> Bucla trimisă ca plan. Serverul nu o începe, iar celula rămâne.
    6. Dovada
           -> namedResult ține SELECT-ul. proseLoopTheServerRejects ține bucla. root@localhost a fost refuzat.
    7. Interviu
           -> Tu numești rezultatul. Optimizerul face planul. SELECT data_value FROM entity_row nu șterge celula. FOR EACH nu pornește.
*/

public class SQL_Declarative_Language {

    /*
    🟢 Începător
        Ce este            propoziția care spune ce valoare vrei, nu cum o cauți
        De ce e nevoie     fără ea, comentariul despre optimizer nu ajunge la server
    🟡 Producție
        Cum se scrie       SELECT data_value FROM entity_row
    🔴 Staff
        Unde               rezultatul se vede la sfârșitul propoziției. Planul îl construiește optimizerul. Celula rămâne. EXPLAIN nu a fost citit în tur.
    Pași
        1. Ține SELECT-ul gata de trimis.
        2. Nu îl amesteca cu FOR EACH.
        3. Nu îl trimite: root@localhost a fost refuzat.
    */
    // Începător: metoda ține cererea, nu pașii de căutare.
    // Producție: public static String namedResult(). Dacă dispare, fast nu mai are propoziția declarativă.
    // Expert: Metoda nu deschide JDBC și nu scrie un plan. Nu ține un lacăt, pentru că șirul nu a fost trimis.
    public static String namedResult() {
        // Începător: clientul poate primi data_value din rândurile care există, iar celulele rămân în tabelă.
        // Producție: SELECT data_value FROM entity_row. Fără șir, rămâne comentariul de curs, pe care serverul nu îl execută.
        // Expert: Nu este FOR EACH și nu este INSERT. INSERT ar adăuga o celulă. Acest SELECT o cere. Fără entity_row, serverul nu are de unde citi.
        return "SELECT data_value FROM entity_row";
        // Începător: metoda a dat cererea înapoi, nu un plan și nu o celulă nouă.
        // Producție: acolada închide namedResult. Șirul nu a plecat către 3306.
        // Expert: Refuzul de autentificare lasă planul NERULAT. Acolada nu este EXPLAIN.
    }

    /*
    🟢 Începător
        Ce este            pașii scriși de tine, pe care serverul nu îi poate începe
        De ce e nevoie     fără ei, pare că optimizerul acceptă parcurgerea dictată
    🟡 Producție
        Cum se scrie       FOR EACH entity_row RETURN data_value
    🔴 Staff
        Unde               eșecul este înainte de orice rând citit. Vecinul este SELECT-ul, care lasă planul optimizerului.
    Pași
        1. Ține textul greșit separat.
        2. Nu îl trimite în tur.
        3. Nu îl folosi ca EXPLAIN.
    */
    // Începător: metoda păstrează textul care nu lasă un rezultat.
    // Producție: public static String proseLoopTheServerRejects(). Dacă dispare, eșecul rămâne o poveste.
    // Expert: Textul nu este un hint de index și nu golește tabela. Celula din Introduction_To_DB nu se schimbă din el.
    public static String proseLoopTheServerRejects() {
        // Începător: niciun rând nu este citit, pentru că serverul nu poate începe acest text.
        // Producție: FOR EACH entity_row RETURN data_value. Dacă linia dispare, nu mai ai pașii greșiți lângă SELECT.
        // Expert: PDF-ul spune că optimizerul alege abordarea între TRUNCATE și DELETE. Acest text îi dictează pașii, deci nu este propoziția declarativă.
        return "FOR EACH entity_row RETURN data_value";
        // Începător: metoda s-a oprit la textul greșit.
        // Producție: acolada închide proseLoopTheServerRejects. Textul a rămas în proces.
        // Expert: Nu compara eșecul cu un WHERE gol. Aici serverul nu are o propoziție pe care să o termine.
    }
}
