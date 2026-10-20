/*
Definiția unei variabile de sesiune (session system variable):
       SET SESSION sql_mode = 'TRADITIONAL' schimbă copia de pe conexiunea curentă, la sfârșitul propoziției. Celelalte conexiuni nu o văd.

Exemplu:
       SqlVariables_Terms.explainSessionVariable()
       La deconectare copia dispare. Globalul rămâne.

Definiția unei variabile globale (global system variable):
       SET GLOBAL max_connections = 1000 schimbă valoarea pe care o copiază conexiunile noi. Sesiunile deja deschise nu sunt rescrise.

Exemplu:
       SqlVariables_Terms.explainGlobalVariable()
       SET SESSION max_connections dă ERROR 1229.

Definiția unei variabile de utilizator (user-defined variable):
       SET @p_emp_no = 0 trăiește pe sesiunea care a atribuit-o, până la deconectare, și nu schimbă sql_mode.

Exemplu:
       SqlVariables_Terms.explainUserVariable()
       Un alt client nu vede 0.

Definiția unei variabile locale (local variable):
       DECLARE v_avg_salary în BEGIN...END moare la END. Nu rămâne pe conexiune după rutină.

Exemplu:
       SqlVariables_Terms.explainLocalVariable()
       SET @p_emp_no încă se vede după CALL.

Definiția unei coloane cu același nume (column):
       Dacă variabila locală are numele coloanei, MySQL citește variabila. Coloana nu filtrează.

Exemplu:
       SqlVariables_Terms.explainColumnNameCollision()
       WHERE xname = xname compară variabila cu ea și întoarce valoarea 'bob'.

Terms & Definitions:
*/
// Începător: glosarul stă în același folder fast și spune ce domeniu are valoarea.
// Producție: package _16_SQL_Variables.fast. Metodele țin șirurile gata de trimis, fără conexiune.
// Expert: Nu redenumește copiii din _16_SQL_Variables și nu copiază acolo definițiile.
package _16_SQL_Variables.fast;

// Începător: clasa execută definițiile: sesiune, global, utilizator, locală, coloană.
// Producție: public final class SqlVariables_Terms. Fără ea, Exemplu: de deasupra nu are metodă.
// Expert: Nu predă parametrul IN/OUT din Variables.pdf. Acela rămâne la rutine.
public final class SqlVariables_Terms {

    /*
    🟢 Începător
        Ce este            copia de sql_mode a conexiunii curente
        De ce e nevoie     fără șir, globalul pare că s-a schimbat odată cu sesiunea
    🟡 Producție
        Cum se scrie       SET SESSION sql_mode = 'TRADITIONAL'
    🔴 Staff
        Unde               sfârșitul propoziției, pe conexiunea care a trimis-o. SET GLOBAL nu o rescrie
    Pași
        1. Înapoiază șirul.
        2. Nu deschide o conexiune.
    */
    // Începător: metoda dă propoziția care schimbă modul doar pe conexiunea asta.
    // Producție: public static String explainSessionVariable(). Dacă dispare, Exemplu: nu mai are metodă.
    // Expert: Nu este SET @p_emp_no și nu este DECLARE. sql_mode nu cere privilegiu special.
    public static String explainSessionVariable() {
        // Începător: conexiunea care primește propoziția lucrează apoi în TRADITIONAL. Un alt client rămâne cum era.
        // Producție: SET SESSION sql_mode = 'TRADITIONAL'. Fără șir, definiția nu are exemplu executabil.
        // Expert: SET LOCAL sql_mode este aceeași copie de sistem, nu o variabilă locală. Linia nu scrie un rând și nu intră în binary log.
        return "SET SESSION sql_mode = 'TRADITIONAL'";
        // Începător: metoda a dat propoziția înapoi și nu a lăsat un rând.
        // Producție: acolada închide explainSessionVariable. Șirul nu a plecat către 3306.
        // Expert: Refuzul root@localhost nu schimbă definiția. Acolada nu este deconectarea.
    }

    /*
    🟢 Începător
        Ce este            limita pe care o vor copia conexiunile noi
        De ce e nevoie     fără șir, slide-ul spune că globalul atinge toate conexiunile deja deschise
    🟡 Producție
        Cum se scrie       SET GLOBAL max_connections = 1000
    🔴 Staff
        Unde               valoarea globală, văzută la connect de o sesiune nouă. Cere SYSTEM_VARIABLES_ADMIN
    Pași
        1. Înapoiază SET GLOBAL.
        2. Nu îl trimite.
    */
    // Începător: metoda dă propoziția care pregătește conectările de după.
    // Producție: public static String explainGlobalVariable(). Dacă dispare, termenul global nu are șir.
    // Expert: Nu este .max_connections() din PDF. Nu alocă memoria join-ului.
    public static String explainGlobalVariable() {
        // Începător: o conexiune nouă pornește de la limita nouă. Conexiunile deja deschise nu își schimbă copiile.
        // Producție: SET GLOBAL max_connections = 1000. Dacă înlocuiești cu SET SESSION, ai ieșit din termen: ERROR 1229.
        // Expert: SET PERSIST ar scrie și mysqld-auto.cnf și tot nu ar rescrie sesiunile deschise. Linia nu ține lacăt.
        return "SET GLOBAL max_connections = 1000";
        // Începător: nicio conexiune deschisă nu a fost închisă de metodă.
        // Producție: acolada închide explainGlobalVariable.
        // Expert: NERULAT pe server nu face din .max_join_size() o alocare de memorie. Acolada nu este SET PERSIST.
    }

    /*
    🟢 Începător
        Ce este            numărul inventat de client, ținut pe conexiunea lui
        De ce e nevoie     fără @, numărul pare o variabilă de sistem
    🟡 Producție
        Cum se scrie       SET @p_emp_no = 0
    🔴 Staff
        Unde               sesiunea clientului, până la deconectare. Nu este @@SESSION.sql_mode
    Pași
        1. Înapoiază atribuirea cu @.
        2. Nu o trimite.
    */
    // Începător: metoda dă numărul pe care îl vede propoziția următoare de pe aceeași conexiune.
    // Producție: public static String explainUserVariable(). Dacă dispare, termenul @ nu are șir.
    // Expert: performance_schema.user_variables_by_thread este excepția de citire din 11.4, nu domeniul variabilei.
    public static String explainUserVariable() {
        // Începător: conexiunea ține 0. Un alt client nu îl primește. sql_mode nu se schimbă.
        // Producție: SET @p_emp_no = 0. Neinițializat ar fi fost NULL de tip șir. Dacă linia dispare, rămâne doar SET SESSION.
        // Expert: @ nu poate fi nume de coloană. În afara lui SET, = este comparație. Linia nu se scrie în binary log.
        return "SET @p_emp_no = 0";
        // Începător: metoda nu a lăsat numărul pe alt client.
        // Producție: acolada închide explainUserVariable.
        // Expert: Deconectarea eliberează @. Acolada nu este deconectarea. În tur șirul nu a fost trimis.
    }

    /*
    🟢 Începător
        Ce este            media care moare la sfârșitul blocului
        De ce e nevoie     fără DECLARE, media pare că rămâne pe conexiune ca @
    🟡 Producție
        Cum se scrie       CREATE PROCEDURE pay_batch() BEGIN DECLARE v_avg_salary DECIMAL(10,2); SET v_avg_salary = 0; END
    🔴 Staff
        Unde               BEGIN...END. La END numele dispare. Nu este o coloană nouă
    Pași
        1. Înapoiază rutina.
        2. Nu o crea pe server.
    */
    // Începător: metoda dă rutina în care media nu iese din bloc.
    // Producție: public static String explainLocalVariable(). Dacă dispare, termenul local nu are șir.
    // Expert: DECLARE singur, în afara rutinei, nu deschide un bloc. SET LOCAL sql_mode nu este această variabilă.
    public static String explainLocalVariable() {
        // Începător: în procedură media devine 0 și dispare la END. Tabela nu primește coloana.
        // Producție: CREATE PROCEDURE pay_batch() BEGIN DECLARE v_avg_salary DECIMAL(10,2); SET v_avg_salary = 0; END. Dacă linia dispare, nu mai ai domeniul blocului.
        // Expert: Un bloc interior cu același nume acoperă acesta. Prepared statement din rutină nu poate referi v_avg_salary. În tur nu a fost trimisă.
        return "CREATE PROCEDURE pay_batch() BEGIN DECLARE v_avg_salary DECIMAL(10,2); SET v_avg_salary = 0; END";
        // Începător: metoda nu a lăsat media pe conexiune.
        // Producție: acolada închide explainLocalVariable.
        // Expert: Refuzul root@localhost nu creează procedura. Acolada nu este END-ul care scapă variabila.
    }

    /*
    🟢 Începător
        Ce este            o variabilă locală care poartă numele coloanei
        De ce e nevoie     fără propoziție, pare că WHERE citește coloana
    🟡 Producție
        Cum se scrie       SELECT xname, id INTO newname, xid FROM table1 WHERE xname = xname, în blocul care a declarat xname
    🔴 Staff
        Unde               MySQL citește variabila. Coloana table1.xname nu filtrează. Exemplul manualului întoarce 'bob'
    Pași
        1. Înapoiază rutina din 15.6.4.2.
        2. Nu o trimite: table1 poate lipsi, iar autentificarea a căzut.
    */
    // Începător: metoda ține cazul în care numele comun nu mai citește coloana.
    // Producție: public static String explainColumnNameCollision(). Dacă dispare, termenul coloană nu are șir.
    // Expert: Nu este un index pe xname. Nu este @xname. Pagina 15.6.4.2 este sursa, nu slide-ul de curs.
    public static String explainColumnNameCollision() {
        // Începător: procedura întoarce 'bob' din variabilă, indiferent ce stă în coloana xname. Rândurile din table1 nu se filtrează după coloană.
        // Producție: CREATE PROCEDURE sp1(x VARCHAR(5)) BEGIN DECLARE xname VARCHAR(5) DEFAULT 'bob'; DECLARE newname VARCHAR(5); DECLARE xid INT; SELECT xname, id INTO newname, xid FROM table1 WHERE xname = xname; END. Dacă scoți DECLARE xname, WHERE ar vedea coloana.
        // Expert: Aceeași citire a variabilei apare și într-un cursor SELECT xname. Nu este un lacăt pe table1. În tur nu a fost trimisă.
        return "CREATE PROCEDURE sp1(x VARCHAR(5)) BEGIN DECLARE xname VARCHAR(5) DEFAULT 'bob'; DECLARE newname VARCHAR(5); DECLARE xid INT; SELECT xname, id INTO newname, xid FROM table1 WHERE xname = xname; END";
        // Începător: metoda s-a oprit la text. Niciun rând din table1 nu a fost citit.
        // Producție: acolada închide explainColumnNameCollision.
        // Expert: Lipsa lui table1 în tur nu schimbă regula numelui. Acolada nu creează procedura.
    }
}
