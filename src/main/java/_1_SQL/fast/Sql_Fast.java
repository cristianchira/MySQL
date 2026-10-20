/*
TOPIC:
    Alegi propoziția pe care o execută serverul, nu celula stocată și nu pașii scriși de tine.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să arăți o valoare venită de la server fără să lași un rând în tabelă.
MECHANISM:
    SQL.statementTheServerExecutes ține SELECT 1 AS data_value. Serverul, dacă o primește, întoarce rândul la sfârșitul propoziției. main doar o tipărește.
LOOKALIKES:
    Introduction_To_DB ține celula la întâlnirea rândului cu coloana, fără nicio propoziție. Această propoziție nu creează celula.
BOOK THEORY:
    Manualul 9.7, secțiunea 1.2.1, deschisă în tur: SQL este limbajul cu care accesezi colecția structurată, iar serverul execută. PDF-ul de curs spune că optimizerul alege abordarea pentru TRUNCATE și DELETE și că sintaxa este mai multe tipuri de propoziții. Nu conține SELECT 1 AS data_value.
BOOK EXAMPLE:
    main
REPO DEMO:
    src/main/java/_1_SQL/SQL.java
BREAK LAB:
    SQL.proseLoopTheServerRejects ține FOR EACH entity_row RETURN data_value. main nu o trimite.
TRADE-OFFS:
    Valoarea care nu trebuie văzută de alt client rămâne în aplicație. Celula stocată este Introduction_To_DB.
ANTI-PATTERNS:
    Să tipărești comentariul de curs și să spui că serverul l-a executat.
STAFF QUESTIONS:
    Întrebările 1–50 din Sql_Staff_Interview.
NOVEL TRANSFER:
    Cifra de control de pe un colet: propoziția o arată, raftul nu primește un rând.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei propoziții SQL (SQL statement):
       Propoziția este textul pe care îl execută serverul. SELECT 1 AS data_value cere un rând de rezultat și nu scrie o celulă.

Exemplu:
       Sql_Terms.explainSqlStatement()
       Tabelele rămân neschimbate după ce rezultatul a plecat.

Definiția unei baze de date (database):
       Baza de date este colecția structurată. Serverul o prelucrează. Propoziția de mai sus nu adaugă o colecție.

Exemplu:
       Sql_Terms.explainDatabase()
       SELECT 1 AS data_value nu creează o bază.

Definiția unei tabele (table):
       Tabela ține rândurile. Propoziția care nu o numește nu citește și nu scrie în ea.

Exemplu:
       Sql_Terms.explainTable()
       SELECT data_value FROM entity_row numește tabela. SELECT 1 AS data_value nu o numește.

Definiția unei coloane (column):
       Coloana este atributul vertical stocat. Aliasul din rezultat nu creează atributul în schemă.

Exemplu:
       Sql_Terms.explainColumn()
       data_value din SELECT 1 AS data_value este numele din rezultat.

Definiția unui rând (row):
       Rândul stocat este înregistrarea din tabelă. Rândul de rezultat trăiește până pleacă la client.

Exemplu:
       Sql_Terms.explainRow()
       Un 1 primit de client nu este un record din Introduction_To_DB.

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei valoarea de la server, fără celulă nouă -> main -> SELECT 1 AS data_value tipărit -> nu garantează că serverul a primit șirul
    2. Eșecul cel mai mic
           -> DECIDE scrii tu parcurgerea -> SQL.proseLoopTheServerRejects -> FOR EACH entity_row RETURN data_value -> nu garantează un mesaj de eroare în tur
    3. Granița
           -> Propoziția este mecanismul. System.out este doar afișarea șirului. Workbench nu este mecanismul.
    4. Alternativa
           -> Introduction_To_DB când întrebarea este celula. Aplicația când niciun alt client nu vede valoarea.
    5. Anti-patternul
           -> Comentariul de curs trimis ca propoziție. Serverul nu îl execută.
    6. Dovada
           -> main tipărește șirul din statementTheServerExecutes. Planul nu a fost citit: root@localhost a fost refuzat.
    7. Interviu
           -> Propoziția cere rezultatul. SELECT 1 AS data_value nu scrie o celulă. Bucla FOR EACH nu pornește serverul.

🟢 Începător
    Ce este            exemplul minim: o propoziție care cere o valoare și nu umple o tabelă
    De ce e nevoie     fără șirul din SQL.java, comentariul de curs pare că este deja propoziția
🟡 Producție
    Cum se scrie       System.out.println(SQL.statementTheServerExecutes())
🔴 Staff
    Unde               tipărirea nu este execuția. Vecinul este celula din Introduction_To_DB. Efectul pe server rămâne NERULAT în tur.
Pași
    1. Ia propoziția din SQL.statementTheServerExecutes.
    2. O tipărește.
    3. Nu deschide o conexiune și nu trimite bucla FOR EACH.
*/
// Începător: lecția scurtă stă lângă clasa care deja descrie interogarea, în folderul fast.
// Producție: package _1_SQL.fast. Dacă linia dispare, main nu mai este lângă SQL.java.
// Expert: Pachetul nu alege între celulă și rezultat. _1_Introduction_To_DB și _2_SQL_Declarative_Language rămân folderele lor.
package _1_SQL.fast;

// Începător: clasa pornește o singură propoziție, fără să creeze o tabelă.
// Producție: public final class Sql_Fast. Fără ea, nu există main pentru exemplul minim.
// Expert: Nu predă DDL, DML, DCL sau TCL. Copiii folderului rămân clasele deja scrise.
public final class Sql_Fast {
    // Începător: pornește exemplul și scrie propoziția, nu un rând dintr-o tabelă.
    // Producție: public static void main(String[] args). args nu este o propoziție și nu schimbă serverul.
    // Expert: Nu cheamă proseLoopTheServerRejects. Nu deschide JDBC. Tipărirea nu ține loc de COMMIT.
    public static void main(String[] args) {
        // Începător: pe ecran apare textul pe care serverul l-ar putea rula, iar tabelele nu se umplu de aici.
        // Producție: System.out.println(SQL.statementTheServerExecutes()), adică SELECT 1 AS data_value. Fără linie, exemplul minim dispare.
        // Expert: println nu trimite propoziția la 3306. Celula din Introduction_To_DB tot nu apare. Un SELECT care numește o tabelă este alt folder.
        System.out.println(_1_SQL.SQL.statementTheServerExecutes());
        // Începător: exemplul s-a oprit după ce a arătat propoziția.
        // Producție: acolada închide main. args rămâne nefolosit, pentru că URL-ul nu stă în argumente.
        // Expert: Refuzul root@localhost nu este un rezultat al acestei acolade. Bucla FOR EACH rămâne în cealaltă metodă.
    }
    // Începător: lecția scurtă se oprește la această propoziție.
    // Producție: acolada închide Sql_Fast. Nu mai există o a doua clasă Fast.
    // Expert: Cine întreabă ce stă într-o celulă iese la Introduction_To_DB. Cine întreabă cine alege pașii iese la limbajul declarativ.
}
