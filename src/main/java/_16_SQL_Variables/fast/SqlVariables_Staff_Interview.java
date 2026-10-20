/*
TOPIC:
    Cincizeci de întrebări despre domeniul variabilei: copia de sesiune, globalul, @, DECLARE și ERROR 1229.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied for user 'root'@'localhost' (using password: NO). Efectul pe server rămâne NERULAT AICI. EXPLAIN nu a fost citit.
INTENT:
    Recunoaștere din SQL sau din eroare, momentul în care se vede copia, deosebirea de global și de @, eșecul în producție și alegerea față de aplicație.
MECHANISM:
    Serverul execută SET SESSION sql_mode = 'TRADITIONAL' doar dacă o primește. Copia conexiunii se schimbă la sfârșitul propoziției. SET GLOBAL nu o rescrie.
LOOKALIKES:
    SET GLOBAL max_connections pregătește conexiunile noi. SET @p_emp_no nu este sql_mode. DECLARE moare la END. SET SESSION max_connections este ERROR 1229.
BOOK THEORY:
    Manualul 9.7, 15.7.6.1, 7.1.9, 11.4, 15.6.4.1 și 15.6.4.2, deschise în tur. Global+Variables.pdf spune că globalul atinge toate conexiunile și că max_join_size este memorie. Ambele contrazic manualul. În SESSION_VARIABLES nu există PDF.
BOOK EXAMPLE:
    SQL_VARIABLES.sessionCopyTheCurrentConnectionKeeps și SQL_VARIABLES.breakSessionKeywordOnGlobalOnly
REPO DEMO:
    src/main/java/_16_SQL_Variables/SQL_VARIABLES.java
BREAK LAB:
    SET SESSION max_connections = 1000
TRADE-OFFS:
    SET GLOBAL sau SET PERSIST pentru conectările noi. DECLARE pentru END. @ pentru propoziția următoare de pe aceeași conexiune. Aplicația când serverul nu citește valoarea.
ANTI-PATTERNS:
    SET GLOBAL sql_mode ca să schimbi conexiunea deja deschisă, sau @ folosit ca nume de coloană.
STAFF QUESTIONS:
    Întrebările 1–50 de mai jos. Cheia este scrisă. Nu există main care să o citească.
NOVEL TRANSFER:
    Tejgheaua unei farmacii: TRADITIONAL doar pe conexiunea deschisă, fără să schimbe tura care se conectează după.
SPACED REVIEW:
    O zi: 11–20. Șapte zile: 31–40. Treizeci de zile: 41–50.

Definiția unei variabile de sesiune (session system variable):
       SET SESSION sql_mode = 'TRADITIONAL' schimbă copia conexiunii curente.

Exemplu:
       SqlVariables_Terms.explainSessionVariable()
       Celelalte conexiuni nu își schimbă modul.

Definiția unei variabile globale (global system variable):
       SET GLOBAL max_connections = 1000 pregătește conexiunile noi.

Exemplu:
       SqlVariables_Terms.explainGlobalVariable()
       Sesiunile deschise nu sunt rescrise.

Definiția unei variabile de utilizator (user-defined variable):
       SET @p_emp_no = 0 rămâne pe sesiune până la deconectare.

Exemplu:
       SqlVariables_Terms.explainUserVariable()
       Nu schimbă sql_mode.

Definiția unei variabile locale (local variable):
       DECLARE v_avg_salary moare la END.

Exemplu:
       SqlVariables_Terms.explainLocalVariable()
       Nu rămâne după CALL.

Definiția unei coloane cu același nume (column):
       Numele comun face SELECT-ul să citească variabila locală.

Exemplu:
       SqlVariables_Terms.explainColumnNameCollision()
       WHERE xname = xname nu filtrează coloana.

🟢 Începător
    Ce este            cincizeci de întrebări despre domeniul variabilei
    De ce e nevoie     fără cheie scrisă, banca nu se poate corecta
🟡 Producție
    Cum se scrie       SET SESSION sql_mode = 'TRADITIONAL', iar eșecul este SET SESSION max_connections = 1000
🔴 Staff
    Unde               copia se vede la sfârșitul lui SET. Nu există main de recall.
Pași
    1. Recunoaște textul.
    2. Spune cine mai vede valoarea.
    3. Desparte sesiunea de global, de @ și de DECLARE.

Recunoaștere din SQL, EXPLAIN sau eroare:
    1. Enunț: În jurnal vezi SET SESSION sql_mode = 'TRADITIONAL'. Ce s-a schimbat dacă serverul a terminat propoziția?
       Capcană: S-a schimbat modul fiecărei conexiuni deschise.
       Cheie: Copia de pe conexiunea care a trimis SET. Globalul rămâne. Nu s-a scris un rând. Efectul este la sfârșitul propoziției. În tur jurnalul nu a fost citit.
    2. Enunț: În jurnal vezi SET GLOBAL max_connections = 1000. Ce conexiune își schimbă o copie deja luată?
       Capcană: Conexiunea care a trimis SET, pentru că ea a executat propoziția.
       Cheie: Niciuna. Globalul se copiază la connect. Sesiunile deschise, inclusiv aceasta, rămân. max_connections nici nu are copie de sesiune.
    3. Enunț: Vezi SET @p_emp_no = 0. Este @@SESSION.sql_mode?
       Capcană: @ este prescurtarea lui @@SESSION.
       Cheie: Este variabila de utilizator, pe sesiune, cu tip întreg după 0. sql_mode nu se schimbă. Alt client nu vede @p_emp_no.
    4. Enunț: Primești refuz de sintaxă pe DECLARE v_avg_salary DECIMAL(10,2) trimis singur. Ce nu s-a creat?
       Capcană: Serverul a creat o variabilă de sesiune cu acest nume.
       Cheie: DECLARE stă într-un BEGIN...END. Trimis singur, nu deschide blocul. Nu apare nici @, nici coloană. În tur nu a fost trimis.
    5. Enunț: Eroarea este ERROR 1229 (HY000) pe SET SESSION max_connections = 1000. Ce rămâne neschimbat?
       Capcană: S-a creat totuși copia de sesiune, iar eroarea este doar un avertisment.
       Cheie: max_connections este doar globală. Propoziția eșuează întreg. Nu se schimbă nicio variabilă și nu se scrie mysqld-auto.cnf.
    6. Enunț: SET GLOBAL version = 'abc' dă ERROR 1238. Ce fel de variabilă ai atins?
       Capcană: version este de sesiune și acceptă orice șir.
       Cheie: version este read only. SET nu o schimbă. Nu este @version.
    7. Enunț: SHOW VARIABLES LIKE 'sql_mode' nu are GLOBAL în text. Ce valoare întoarce?
       Capcană: Întoarce mereu globalul, pentru că lipsește SESSION.
       Cheie: Fără GLOBAL sau SESSION, SHOW VARIABLES întoarce valorile de sesiune. Ca să vezi globalul, scrii SHOW GLOBAL VARIABLES.
    8. Enunț: Vezi SET LOCAL sql_mode = 'TRADITIONAL'. Este DECLARE?
       Capcană: LOCAL creează o variabilă locală numită sql_mode.
       Cheie: Pentru o variabilă de sistem, SET LOCAL este sinonim cu SET SESSION. Variabila locală se declară cu DECLARE în bloc.
    9. Enunț: Slide-ul scrie .max_join_size() și spune că alocă memoria join-ului. Ce recunoști?
       Capcană: Este o funcție care rezervă memorie pe conexiune.
       Cheie: Nu este funcție. max_join_size limitează accesările de rânduri. Peste estimare, propoziția este respinsă. O valoare diferită de DEFAULT pune sql_big_selects pe 0.
    10. Enunț: Access denied for user 'root'@'localhost' (using password: NO) apare înainte de orice SET. Ce variabilă s-a schimbat?
        Capcană: Serverul a aplicat SET SESSION și apoi a refuzat afișarea.
        Cheie: Refuzul este la conectare. Nicio propoziție din folder nu a fost trimisă. Efectul rămâne NERULAT AICI.

Rânduri, index, lacăt și momentul în care se vede efectul:
    11. Enunț: După SET SESSION sql_mode = 'TRADITIONAL', ce rând nou există într-o tabelă?
        Capcană: Un rând de configurare, vizibil după COMMIT.
        Cheie: Nu este numită o tabelă. Nu există rând nou. Copia de sesiune se vede la sfârșitul propoziției, pe conexiunea care a trimis-o.
    12. Enunț: Ce lacăt ține SET SESSION după ce propoziția s-a terminat?
        Capcană: Ține lacăt pe toate conexiunile până la deconectare, ca să nu se schimbe modul.
        Cheie: Nu există rând de ținut. Propoziția nu lasă un lacăt de rând. Copia stă în sesiune, nu într-un index.
    13. Enunț: Ce index scurtează SET @p_emp_no = 0?
        Capcană: Un index pe p_emp_no, pentru că numele pare o coloană.
        Cheie: Nu este o coloană. Nu există index de ales. EXPLAIN nu a fost citit în tur, iar textul nu numește o tabelă.
    14. Enunț: O a doua sesiune face SELECT @p_emp_no după ce prima a executat SET @p_emp_no = 0. Ce primește, în afara excepției din manual?
        Capcană: Primește 0, pentru că variabila este globală.
        Cheie: Primește NULL: variabila neinițializată pe sesiunea ei, tip șir. Excepția este un cont care citește performance_schema.user_variables_by_thread, nu un al doilea client obișnuit.
    15. Enunț: SET intră în binary log?
        Capcană: Da, ca orice schimbare, ca să prindă și replica copia.
        Cheie: Un SET care atribuie variabile nu se scrie în binary log. Ca să schimbi și replica, rulezi propoziția pe fiecare host. În tur nu a fost trimisă.
    16. Enunț: Când vezi sql_mode nou după SET SESSION: la primirea textului, la COMMIT sau la sfârșitul propoziției?
        Capcană: La COMMIT, pentru că autocommit face configurarea durabilă.
        Cheie: La sfârșitul propoziției, pe conexiunea curentă. autocommit nu transformă copia într-un rând. COMMIT nu este momentul.
    17. Enunț: Rulezi SET GLOBAL sql_mode = 'TRADITIONAL' și imediat SELECT @@SESSION.sql_mode pe aceeași conexiune. Ce vezi față de înainte?
        Capcană: Vezi TRADITIONAL, pentru că tu ai schimbat globalul.
        Cheie: Vezi copia luată la connect. Globalul nou îl primesc conexiunile care se deschid după. Aceasta nu este rescrisă.
    18. Enunț: SELECT @p_emp_no înainte de orice SET. Ce valoare și ce tip are?
        Capcană: Eroare că variabila nu este declarată.
        Cheie: NULL, tip șir. Nu este o coloană lipsă. SET @p_emp_no = 0 îi schimbă apoi tipul în întreg.
    19. Enunț: Clientul se deconectează după SET @p_emp_no = 0. Ce se eliberează?
        Capcană: Se eliberează și @@GLOBAL.sql_mode.
        Cheie: Variabilele @ ale clientului. Copia de sesiune moare odată cu sesiunea. Globalul rămâne pe server până la o altă atribuire sau până la oprire, dacă nu a fost persistat.
    20. Enunț: Un SET are două atribuiri și a doua eșuează. Ce păstrează prima?
        Capcană: Prima rămâne aplicată, iar a doua se sare.
        Cheie: Eșuează tot SET-ul. Nicio variabilă nu se schimbă și mysqld-auto.cnf nu se schimbă. Nu este un rând pe jumătate scris.

Deosebire de mecanismul vecin:
    21. Enunț: De ce SET SESSION sql_mode nu este SET GLOBAL sql_mode?
        Capcană: Sunt sinonime. GLOBAL este doar forma care cere privilegiu.
        Cheie: SESSION schimbă copia conexiunii curente. GLOBAL schimbă valoarea copiată la connect și nu atinge sesiunile deschise. Privilegiul nu le face același domeniu.
    22. Enunț: De ce DECLARE v_avg_salary nu este SET @p_emp_no = 0?
        Capcană: Ambele mor la END, pentru că sunt variabile de utilizator.
        Cheie: DECLARE moare la END. @ rămâne pe sesiune până la deconectare și se vede și după CALL. Doar variabilele de sistem pot fi globale. Locala nu.
    23. Enunț: De ce SET LOCAL sql_mode nu declară o variabilă locală?
        Capcană: Cuvântul LOCAL este DECLARE scris altfel.
        Cheie: SET LOCAL pe o variabilă de sistem este sesiunea. DECLARE este un alt enunț, doar în BEGIN...END, și poate declara și cursor sau handler.
    24. Enunț: SET @col = 'c1' apoi SELECT @col FROM t. De ce nu vezi coloana c1?
        Capcană: @ înlocuiește identificatorul, deci lista de coloane devine c1.
        Cheie: @ întoarce valoarea, textul c1, un rând. Nu este numele coloanei. Ca să folosești numele, construiești textul și îl PREPARE. SELECT `@col` caută o coloană cu acest nume.
    25. Enunț: De ce max_connections nu se comportă ca sql_mode la SET SESSION?
        Capcană: Orice variabilă din SHOW VARIABLES are și sesiune.
        Cheie: max_connections este doar globală. SET SESSION max_connections este ERROR 1229. sql_mode are și global, și sesiune.
    26. Enunț: Variables.pdf vorbește despre IN și OUT. Este parametrul o variabilă de sesiune?
        Capcană: OUT rămâne pe conexiune ca @@SESSION.
        Cheie: Parametrul comunică între rutină și apelant. Nu este SET SESSION sql_mode. Folderul parametrilor este rutina, nu domeniul variabilei de sistem.
    27. Enunț: De ce SET PERSIST max_connections = 1000 nu este un SET SESSION care supraviețuiește?
        Capcană: PERSIST mută limita pe sesiunea curentă și o salvează.
        Cheie: PERSIST scrie mysqld-auto.cnf și setează globalul de rulare. Nu rescrie sesiunile deschise. Cere SYSTEM_VARIABLES_ADMIN sau SUPER. Sesiunea curentă tot nu primește o copie de max_connections.
    28. Enunț: Într-o expresie scrii @@sql_mode, fără GLOBAL și fără SESSION. Ce primești?
        Capcană: Primești mereu globalul, pentru că lipsește SESSION.
        Cheie: @@var întoarce sesiunea dacă există, altfel globalul. SET @@sql_mode, în schimb, se referă mereu la sesiune.
    29. Enunț: SET GLOBAL sort_buffer_size = 1000000, sort_buffer_size = 1000000. Ce primește a doua atribuire?
        Capcană: A doua este sesiune, pentru că nu repetă GLOBAL.
        Cheie: Ultimul cuvânt GLOBAL, PERSIST, PERSIST_ONLY sau SESSION se aplică atribuirilor următoare care nu au cuvânt. A doua este tot globală. @@GLOBAL. s-ar fi aplicat doar variabilei imediat următoare.
    30. Enunț: De ce un prepared statement din procedură nu poate citi v_avg_salary?
        Capcană: Prepared statement vede localele, pentru că a fost creat în rutină.
        Cheie: Prepared statement trăiește pe sesiune și poate fi executat după END, când locala nu mai există. Nici parametrul rutinei nu intră în PREPARE.

Eșec în producție:
    31. Enunț: Un job de noapte rulează SET SESSION max_connections = 1000 ca să își mărească limita. Ce se întâmplă?
        Capcană: Jobul primește 1000 de conexiuni doar pentru el.
        Cheie: ERROR 1229. Limita nu se schimbă. Ca să schimbi limita pentru conectările de după, îți trebuie SET GLOBAL și SYSTEM_VARIABLES_ADMIN, iar jobul deja conectat tot nu capătă o copie de sesiune.
    32. Enunț: SET GLOBAL sql_log_bin = ON. Ce spune manualul?
        Capcană: sql_log_bin este global, deci propoziția pornește logul pentru toți.
        Cheie: ERROR 1228: este variabilă de SESSION și nu poate fi folosită cu SET GLOBAL. Sesiunea ei este restricționată și cere SESSION_VARIABLES_ADMIN.
    33. Enunț: Doi operatori schimbă sql_mode cu SET GLOBAL, fiecare crezând că își repară tejgheaua deschisă. Ce rămâne pe tejghele?
        Capcană: Ambele tejghele primesc ultimul global.
        Cheie: Tejghelele deschise își păstrează copia de la connect. Ultimul SET GLOBAL îl copiază doar cine se conectează după. Binary log nu a primit SET-ul.
    34. Enunț: SET @a = 1, SESSION sql_mode = 'TRADITIONAL' eșuează la a doua atribuire. Ce valoare are @a?
        Capcană: @a rămâne 1, pentru că prima atribuire a reușit.
        Cheie: Tot SET-ul eșuează. @a rămâne cum era înainte de propoziție. Nu se schimbă nici sql_mode.
    35. Enunț: Procedura declară xname DEFAULT 'bob' și face WHERE xname = xname. De ce filtrul nu vede coloana?
        Capcană: Coloana are prioritate, pentru că stă în tabelă.
        Cheie: MySQL citește variabila. Comparația este 'bob' = 'bob'. newname primește 'bob' indiferent de table1.xname. Nu este un index care ascunde coloana.
    36. Enunț: SET @t = 'table1' apoi SELECT * FROM @t. De ce nu se deschide table1?
        Capcană: @ ține loc de identificator dacă valoarea este un nume.
        Cheie: @ nu este identificator de tabelă. Propoziția nu citește table1. Ca să construiești textul, îl PREPARE din șir.
    37. Enunț: SELECT @a, @a := @a + 1. Ce ordine garantează serverul?
        Capcană: Întâi citește @a, apoi adună, pe fiecare rând.
        Cheie: Ordinea este nedefinită. Nu citi și nu atribui aceeași variabilă de utilizator în aceeași propoziție. În 9.7, atribuirea în afara lui SET cu := este încă acceptată și este anunțată pentru scoatere.
    38. Enunț: SET @n = 10 apoi SELECT ... LIMIT @n. De ce nu este limita 10?
        Capcană: @ funcționează în orice expresie, deci și în LIMIT.
        Cheie: LIMIT cere un literal. Variabila de utilizator nu este acceptată acolo. Nu este o problemă de index.
    39. Enunț: În rutină faci PREPARE din un SELECT ... INTO v_avg_salary. De ce cade?
        Capcană: INTO nu este permis decât în afara procedurii.
        Cheie: Locala nu poate fi referită într-un prepared statement. Domeniul prepared statement este sesiunea, nu blocul. SELECT ... INTO local_var nu se pregătește.
    40. Enunț: Crezi că SET GLOBAL max_join_size = 1000 rezervă memorie pentru join. Ce se întâmplă de fapt cu un SELECT mare?
        Capcană: Join-ul primește mai multă memorie și trece.
        Cheie: max_join_size nu este memorie. Dacă estimarea de accesări de rânduri trece de limită, propoziția este respinsă. Setarea diferită de DEFAULT pune sql_big_selects pe 0. Dacă pui sql_big_selects la loc, max_join_size este ignorat.

Trade-off, inclusiv când rămâi în aplicație:
    41. Enunț: Numărul de rețete nu intră în nicio propoziție. Îl pui în @p_emp_no?
        Capcană: Da, altfel serverul nu îl ține minte.
        Cheie: Rămâne în aplicație. @ ar fi vizibil oricărei propoziții de pe conexiune și ar dispărea la deconectare, fără să fie nevoie de el pe server.
    42. Enunț: Doar tejgheaua deja deschisă trebuie să refuze date invalide. Ce propoziție alegi?
        Capcană: SET GLOBAL sql_mode = 'TRADITIONAL', ca să fie sigur pentru toată instanța.
        Cheie: SET SESSION sql_mode = 'TRADITIONAL'. Globalul rămâne. Tejgheaua de alături nu se schimbă. La deconectare copia moare.
    43. Enunț: Următoarele scanere care se conectează trebuie să pornească cu o limită nouă. Ce nu este de ajuns?
        Capcană: SET SESSION max_connections pe scannerul deja conectat.
        Cheie: Este ERROR 1229. Îți trebuie SET GLOBAL max_connections și SYSTEM_VARIABLES_ADMIN. Scannerul deja conectat nu primește o copie nouă de max_connections. Dacă limita trebuie să treacă de restart, SET PERSIST, care tot nu rescrie sesiunile deschise.
    44. Enunț: Media se calculează în procedură și nu trebuie văzută după CALL. De ce nu SET @p_emp_no?
        Capcană: @ moare la END, la fel ca DECLARE.
        Cheie: @ rămâne până la deconectare. DECLARE v_avg_salary moare la END. Dacă o lași în @, propoziția următoare de pe conexiune încă o vede.
    45. Enunț: Propoziția de după CALL trebuie să citească numărul produs în procedură. De ce DECLARE nu ajunge?
        Capcană: DECLARE este vizibil pe sesiune cât timp conexiunea este deschisă.
        Cheie: La END locala nu mai există. Îți trebuie @, sau un parametru OUT al rutinei. OUT nu este @@SESSION.
    46. Enunț: Vrei ca sql_mode TRADITIONAL să rămână după restart, pentru conexiunile noi. Ce alegi față de SET SESSION?
        Capcană: SET SESSION, pentru că sesiunea se salvează în binary log.
        Cheie: SET SESSION moare cu conexiunea și nu intră în binary log. SET PERSIST sql_mode scrie mysqld-auto.cnf și setează globalul. Sesiunea deja deschisă tot nu este rescrisă.
    47. Enunț: Un cont fără SYSTEM_VARIABLES_ADMIN trebuie să își schimbe sql_mode. Poate?
        Capcană: Nu. Orice SET pe o variabilă de sistem cere privilegiul global.
        Cheie: SET SESSION sql_mode nu cere privilegiu special. SET GLOBAL cere SYSTEM_VARIABLES_ADMIN sau SUPER. sql_log_bin este alt caz: sesiune restricționată, SESSION_VARIABLES_ADMIN.
    48. Enunț: Două propoziții pe aceeași conexiune trebuie să își paseze un întreg, iar nimeni altcineva nu trebuie să îl vadă. De ce nu o coloană temporară într-o tabelă permanentă?
        Capcană: Tabela ține valoarea mai sigur decât @ și dispare singură la deconectare.
        Cheie: Rândul rămâne până îl ștergi și poate fi văzut de alt client care are dreptul. @ moare la deconectare și nu se vede din altă sesiune. Dacă nici a doua propoziție nu trebuie să îl citească pe server, rămâne în aplicație.
    49. Enunț: Slide-ul spune că 100 de utilizatori înseamnă exact 100 de sesiuni. Cum numeri de fapt copiile de sql_mode?
        Capcană: Câte conturi, atâtea copii.
        Cheie: Câte conexiuni, atâtea sesiuni. Un cont poate ține mai multe conexiuni, fiecare cu copia ei. Contul nu este conexiunea.
    50. Enunț: La interviu ai un minut: efect, propoziție, vecin. Ce spui fără să amesteci @ cu sql_mode?
        Capcană: Închizi cu „variabilele sunt globale” și treci la CREATE PROCEDURE.
        Cheie: SET SESSION sql_mode = 'TRADITIONAL' schimbă copia conexiunii la sfârșitul propoziției și nu scrie un rând. SET GLOBAL nu rescrie sesiunile deschise. SET SESSION max_connections este ERROR 1229. @ și DECLARE sunt alte domenii: sesiunea clientului până la deconectare, respectiv blocul până la END.
*/
// Începător: banca ține cincizeci de întrebări despre domeniul variabilei, nu un main care să spună răspunsul.
// Producție: package _16_SQL_Variables.fast. Nu adăuga main de recall.
// Expert: Întrebările nu se lipesc pe JOIN sau pe TRUNCATE. Vecinul lor este globalul față de copia de sesiune.
package _16_SQL_Variables.fast;

// Începător: clasa nu trimite propoziția și nu citește cheia la cerere.
// Producție: public final class SqlVariables_Staff_Interview. Corpul rămâne fără main.
// Expert: Cele cincizeci stau în comentariu, cu cheia scrisă. Nu șterge din ele ca să rămână un alt număr.
public final class SqlVariables_Staff_Interview {
    // Începător: nu există o a doua propoziție în corp.
    // Producție: acolada închide SqlVariables_Staff_Interview.
    // Expert: sessionCopyTheCurrentConnectionKeeps rămâne dovada din SQL_VARIABLES.java. Banca nu o înlocuiește.
}
