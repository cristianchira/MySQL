/*
TOPIC:
    Cincizeci de întrebări despre rândul care rămâne când alegi îmbinarea, produsul, lipsa sau lipirea a două blocuri.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Recunoașterea din SQL sau din Unknown column, momentul în care rândul cade din rezultat, deosebirea de LEFT, UNION și subcerere, eșecul din WHERE pe NULL și alegerea față de aplicație.
MECHANISM:
    INNER pe dept_no scoate managerul fără pereche. LEFT îl păstrează cu NULL. WHERE care respinge NULL poate face inner. UNION ALL păstrează copia. Join-ul dinainte de AVG înmulțește salariul.
LOOKALIKES:
    Subcererea nu este JOIN. DISTINCT nu este HAVING COUNT. NATURAL nu este ON scris. Indexul nu este potrivirea. RIGHT păstrează cealaltă parte.
BOOK THEORY:
    Manualul 9.7, 15.2.13.2, 10.2.1.9, 15.2.14 și 14.19.3, deschise în tur. PDF-urile din _10_JOINs, pe paginile deschise, nu descriu rândul care cade, în afară de Duplicate+Records.pdf pagina 2, care spune că rândurile duplicate sunt identice.
BOOK EXAMPLE:
    Probele din clasele folderului, nu o metodă din această clasă.
REPO DEMO:
    src/main/java/_10_JOINs/JOINs.java
BREAK LAB:
    WHERE d.dept_no IS NULL după INNER, sau IS NULL mutat în ON, sau JOIN fără ON.
TRADE-OFFS:
    LEFT când lipsa rămâne. UNION ALL când copia rămâne. Derived table când media nu are voie să vadă titlul. Aplicația când codul nu este pe acest server.
ANTI-PATTERNS:
    INNER apoi IS NULL, WHERE pe coloana din dreapta după un LEFT scris ca să păstreze stânga, AVG după titles, NATURAL ca să nu scrii cheia.
STAFF QUESTIONS:
    Întrebările 1–50 de mai jos. Cheia este scrisă. Nu există main care să o citească singur.
NOVEL TRANSFER:
    Coletul și raftul: pereche, lipsă, matrice, două liste lipite.
SPACED REVIEW:
    O zi: întrebările 11–20. Șapte zile: 31–40. Treizeci de zile: 41–50.

Definiția unei îmbinări (join):
       Îmbinarea păstrează, din două referințe, rândurile pe care le cere forma scrisă: perechea, stânga cu NULL, sau produsul.

Exemplu:
       Joins_Terms.explainJoin()

Definiția unui index (index hint):
       Indexul poate schimba planul. Nu schimbă rândul care se potrivește.

Exemplu:
       Joins_Terms.explainIndex()

Recunoaștere din SQL, EXPLAIN sau eroare:
    1. Enunț: Vezi INNER JOIN departments_dup d ON m.dept_no = d.dept_no. Ce manager nu este în rezultat?
       Capcană: Toți sunt, iar departamentul lipsă vine cu spații goale.
       Cheie: Managerul al cărui dept_no nu este în departments_dup. NULL nu se potrivește prin =. Tabela dept_manager_dup nu pierde rândul.
    2. Enunț: Vezi JOIN departments d, fără ON, pe dept_manager. Ce ai cerut în MySQL 9.7?
       Capcană: O eroare, pentru că INNER este obligatoriu și cere ON.
       Cheie: Produsul. JOIN, CROSS JOIN și INNER JOIN sunt echivalente sintactic. Fără condiție, fiecare manager stă cu fiecare departament. Nu este CROSS-ul scris, dar este același produs.
    3. Enunț: Ce recunoști în LEFT JOIN departments_dup d ON m.dept_no = d.dept_no WHERE d.dept_no IS NULL?
       Capcană: Este INNER, pentru că WHERE taie mereu stânga.
       Cheie: Anti-join-ul din 15.2.13.2: managerul fără corespondent. Dacă d.dept_no este NOT NULL, căutarea se poate opri după prima potrivire. Nu este IS NULL pus în ON.
    4. Enunț: LEFT JOIN ... ON m.dept_no = d.dept_no WHERE d.dept_name = 'Sales'. Ce se întâmplă cu managerul fără departament?
       Capcană: Rămâne, cu dept_name NULL, lângă rândurile din Sales.
       Cheie: NULL nu este 'Sales'. 10.2.1.9 permite rescrierea ca inner join. Managerul fără departament cade din rezultat.
    5. Enunț: Ce recunoști în SELECT ... FROM employees e, dept_manager dm JOIN departments d ON e.emp_no = d.dept_no?
       Capcană: Trei tabele legate corect, pentru că virgula este un INNER.
       Cheie: JOIN se leagă înaintea virgulei. ON vede dm și d, nu e. Pagina arată Unknown column în on clause.
    6. Enunț: (SELECT e.emp_no AS id FROM employees_dup e) UNION (SELECT m.emp_no FROM dept_manager m) ORDER BY emp_no. Ce nume este greșit?
       Capcană: emp_no este în ambele blocuri, deci ORDER BY îl găsește.
       Cheie: Numele rezultatului vine din primul bloc: id. 15.2.14 arată Unknown column în order clause când ORDER BY folosește numele dinainte de alias.
    7. Enunț: UNION ALL între un bloc cu cinci coloane și unul cu patru. Ce nu formează?
       Capcană: Serverul umple a cincea cu NULL și întoarce rezultatul.
       Cheie: Nu este un singur rezultat. NULL AS se scrie de tine, pe poziție. Pagina deschisă nu dă numărul erorii pentru numărul de coloane; arată că pozițiile trebuie să corespundă ca tip.
    8. Enunț: FROM emp_manager e1 JOIN emp_manager e2 ON e1.emp_no = e2.manager_no. Câte tabele stocate citești?
       Capcană: Două, pentru că sunt două nume în FROM.
       Cheie: Una, emp_manager, prin două referințe. Fără e1 și e2, coloanele nu au referință unică.
    9. Enunț: EXPLAIN arată că dept_no nu folosește un index. Ce ai dovedit despre rândurile din INNER JOIN?
       Capcană: Fără index, potrivirea este greșită și managerul fără departament intră în rezultat.
       Cheie: Ai dovedit un plan, nu altă mulțime de perechi. Perechea o decide ON. Hint-ul USE INDEX nu schimbă rândul care se potrivește.
    10. Enunț: Ce recunoști în SELECT * FROM t1 NATURAL LEFT JOIN t2, când t1 are un rând fără pereche pe coloana comună?
        Capcană: Este INNER, pentru că NATURAL nu știe de OUTER.
        Cheie: Este LEFT cu USING pe toate coloanele comune. Coloana coalizată ia valoarea nenulă. Un nume nou, comun, intră în potrivire fără să scrii ON.

Rânduri, index, lacăt și momentul în care se vede efectul:
    11. Enunț: Când vezi că managerul fără departments_dup a căzut din INNER JOIN?
        Capcană: La COMMIT. Până atunci stă în tranzacție și poate fi citit de alt client ca rând șters.
        Cheie: La sfârșitul SELECT-ului, în rezultat. Tabela nu s-a schimbat. Pagina JOIN nu numește un lacăt ținut după ce rezultatul a plecat.
    12. Enunț: LEFT JOIN nu găsește pereche, iar stânga a trecut de WHERE. Ce rând generează serverul?
        Capcană: Nimic. Lipsa înseamnă că stânga cade.
        Cheie: Un rând în care coloanele din dreapta sunt NULL. 10.2.1.9. Nu este un INSERT în departments_dup.
    13. Enunț: Pui USE INDEX (dept_no) pe dept_manager_dup și lași același ON. Ce se schimbă la managerul fără departament?
        Capcană: Indexul îl păstrează, pentru că găsește mai repede lipsa.
        Cheie: Tot cade la INNER. Hint-ul vorbește cu optimizerul. Nu este o condiție și nu ține un lacăt după rezultat.
    14. Enunț: SELECT ... UNION SELECT ... FOR UPDATE, fără paranteze în jurul blocului cu lacătul. Unde se aplică lacătul, dacă propoziția este acceptată doar cu paranteze?
        Capcană: Pe ambele blocuri, până la COMMIT, pentru că UNION este o singură citire.
        Cheie: 15.2.14: clauza de lacăt se aplică blocului pe care îl urmează, iar blocul trebuie în paranteze. Nu este un tip de join. Pagina JOIN nu transformă INNER în FOR UPDATE.
    15. Enunț: Un FROM cu 61 de referințe, inclusiv un view contopit. Ce limită ai atins?
        Capcană: 61 de rânduri în rezultat.
        Cheie: 61 de tabele într-un join, cum scrie 15.2.13.2, inclusiv contopirea din view sau derived table. Nu este un plafon de rânduri și nu schimbă cine se potrivește.
    16. Enunț: SELECT * cu USING (dept_no) față de același LEFT cu ON. Ce coloană vezi la managerul fără pereche?
        Capcană: Aceleași patru coloane, cu două dept_no NULL.
        Cheie: USING arată o coloană coalizată. La outer join, ea ia valoarea nenulă, aici dept_no din stânga. ON arată ambele, iar dreapta este NULL.
    17. Enunț: STRAIGHT_JOIN între manager și departament. Ce ai fixat și ce nu?
        Capcană: Ai fixat perechea: stânga se citește întâi, deci LEFT și INNER dau aceleași rânduri.
        Cheie: Ai fixat ordinea de citire, ca JOIN, nu mulțimea. 10.2.1.9: poate opri transformarea de semijoin și deci un index. Nu păstrează managerul fără departament.
    18. Enunț: UNION fără ALL, iar același rând iese din ambele blocuri. Când dispare copia din rezultat?
        Capcană: La COMMIT, după ce serverul curăță tabelele.
        Cheie: La sfârșitul expresiei de set, în rezultat. Tabelele rămân. ALL ar fi lăsat ambele copii. Un DISTINCT mai la dreapta acoperă un ALL din stânga.
    19. Enunț: ORDER BY în primul bloc al unui UNION, fără LIMIT și fără să fie pe tot rezultatul. Ce ordine garantează?
        Capcană: Rezultatul unit iese în ordinea acelui ORDER BY.
        Cheie: Niciuna pe rezultatul final. 15.2.14: fără LIMIT, ORDER BY din bloc este scos, pentru că nu are efect. Ordinea finală se cere după ultimul bloc.
    20. Enunț: AVG(salary) într-o tabelă derivată grupată pe emp_no, apoi JOIN la departament. Când vezi media?
        Capcană: După COMMIT, scrisă în salaries.
        Cheie: În rezultatul SELECT-ului. salaries nu primește coloana. Un al doilea rând în dept_manager repetă aceeași medie, nu o recalculează cu titluri.

Deosebire de mecanismul vecin:
    21. Enunț: De ce LEFT JOIN nu este INNER JOIN cu un ORDER BY?
        Capcană: Aceleași rânduri, doar că NULL-urile se duc la urmă.
        Cheie: LEFT păstrează managerul fără pereche, cu NULL la dreapta. INNER îl scoate. Ordinea nu îl aduce înapoi.
    22. Enunț: De ce RIGHT JOIN nu este o potrivire diferită de LEFT pe același ON?
        Capcană: Optimizerul citește dreapta întâi, deci rămân alte dept_no.
        Cheie: Aceeași potrivire, altă parte păstrată. 10.2.1.9 spune că RIGHT se rescrie ca LEFT. Manualul recomandă LEFT ca formă portabilă.
    23. Enunț: De ce JOIN fără ON nu este CROSS JOIN declarat, deși în MySQL produsul este același?
        Capcană: Echivalente înseamnă că forma scrisă nu se întreabă la interviu.
        Cheie: Produsul este același. CROSS spune că l-ai cerut. JOIN fără ON poate fi un ON uitat și se citește greșit ca asignare.
    24. Enunț: De ce UNION ALL nu este INNER JOIN între employees_dup și dept_manager?
        Capcană: Ambele aduc emp_no din două tabele, deci același rând lat.
        Cheie: UNION pune blocuri unul sub altul și cere același număr de poziții. JOIN pune coloanele pe același rând, în același FROM. NULL AS nu este o potrivire.
    25. Enunț: De ce UNION fără ALL nu este UNION ALL?
        Capcană: ALL înseamnă „doar rândurile valide”, deci scoate duplicatul mai tare.
        Cheie: Fără ALL, duplicatul cade din rezultat. Cu ALL, rămâne. Nu se îmbunătățește valoarea din first_name.
    26. Enunț: De ce self join pe emp_manager nu este join între dept_manager și departments?
        Capcană: Două nume în FROM sunt mereu două tabele stocate.
        Cheie: e1 și e2 sunt două referințe la emp_manager. departments este altă tabelă. Fără alias, self join-ul nu poate numi coloana.
    27. Enunț: De ce WHERE s.salary > 50000 înainte de AVG nu este HAVING avg_salary > 50000?
        Capcană: Ambele taie salariile mici, deci media coincide.
        Cheie: WHERE scoate rândul înainte de medie. HAVING scoate grupul după medie. 14.19.3 permite aliasul în HAVING. Numerele nu sunt aceleași.
    28. Enunț: De ce SELECT DISTINCT pe employees_dup nu este GROUP BY ... HAVING COUNT(*) > 1?
        Capcană: Ambele găsesc duplicatul, pentru că lista se curăță.
        Cheie: DISTINCT ascunde copia și nu dă copies. HAVING arată grupul care are mai mult de un rând. Niciunul nu șterge din tabelă. UNIQUE ar refuza al doilea INSERT.
    29. Enunț: De ce WHERE dept_no IN (SELECT dept_no FROM departments_dup) nu este INNER JOIN care aduce dept_name?
        Capcană: Aceiași manageri, deci același plan și aceleași coloane.
        Cheie: Subcererea întoarce o listă. Nu pune dept_name lângă emp_no. Același emp_no nu este același plan. NULL în listă schimbă IN, nu generează rândul NULL de la LEFT.
    30. Enunț: De ce NOT EXISTS nu este LEFT JOIN ... WHERE d.dept_no IS NULL?
        Capcană: Aceeași lipsă, deci același rând NULL și același plan.
        Cheie: NOT EXISTS întreabă dacă subcererea întoarce un rând. Anti-join-ul folosește rândul NULL generat de LEFT. Poți lua coloane din dreapta doar în forma cu JOIN.

Eșec în producție:
    31. Enunț: INNER JOIN și WHERE d.dept_no IS NULL. Câți manageri fără departament vezi?
        Capcană: Pe toți, pentru că IS NULL înseamnă lipsă.
        Cheie: Niciunul. INNER a scos rândul înainte ca WHERE să vadă un NULL. Anti-join-ul cere LEFT.
    32. Enunț: IS NULL mutat în ON: ON m.dept_no = d.dept_no AND d.dept_no IS NULL. Cine rămâne?
        Capcană: Doar managerul fără departament.
        Cheie: Toți managerii, cu dreapta NULL. Condiția nu găsește pereche, iar LEFT păstrează stânga. Managerul care are departament nu este tăiat.
    33. Enunț: AVG(s.salary) după JOIN titles, două titluri și un salariu de 1000. Ce intră în medie?
        Capcană: 1000, pentru că AVG știe că titlul nu este salariu.
        Cheie: 1000 de două ori, o dată per titlu, dacă nu ai grupat salariul înainte. Media nu mai este media salariilor. ANY_VALUE pe titlu nu desface înmulțirea.
    34. Enunț: GROUP BY e.emp_no și în SELECT stau t.title și d.dept_name, cu ONLY_FULL_GROUP_BY. Ce eroare poți primi înainte de medie?
        Capcană: Niciuna, pentru că emp_no este cheia omului, deci departamentul și titlul sunt determinate.
        Cheie: ERROR 1055 (42000) dacă title sau dept_name nu depind funcțional de emp_no. Un om cu două titluri sau două departamente nu le determină. Nu este Unknown column.
    35. Enunț: NATURAL JOIN după ce adaugi coloana from_date în ambele tabele. Ce perechi se schimbă?
        Capcană: Niciuna. NATURAL rămâne pe dept_no, coloana de cheie.
        Cheie: Se potrivește și from_date, pentru că NATURAL ia toate numele comune. Doi oameni cu același dept_no și from_date diferit nu mai stau împreună. ON scris doar pe dept_no nu s-ar fi schimbat.
    36. Enunț: t1 JOIN t2 ON (t1.i1 = t3.i3) JOIN t3. De ce cade, deși t3 este în FROM?
        Capcană: Ordinea din FROM nu contează, optimizerul vede toate coloanele.
        Cheie: ON vede doar operanzii lui. t3 încă nu este operand. 15.2.13.2: Unknown column 'i3' in 'on clause'. Rescrierea este JOIN t2 JOIN t3 ON (t1.i1 = t3.i3), cu t1 deja operand prin lanț, sau o paranteză care chiar îi face operanzi.
    37. Enunț: Trei blocuri, UNION ALL apoi UNION DISTINCT. Ce duplicate din stânga mai rămân?
        Capcană: ALL de la început păstrează tot, pentru că se evaluează de la stânga și nu se mai atinge.
        Cheie: DISTINCT acoperă ALL-ul din stânga lui. 15.2.14. Copia pe care voiai să o păstrezi cade din rezultat.
    38. Enunț: Cross join între 2000 de manageri și 9 departamente, citit ca „departamentul actual”. Câte rânduri poți lua drept asignare?
        Capcană: 2000, pentru că fiecare manager are un departament și produsul se strânge singur.
        Cheie: 18000, dacă ambele tabele au exact atât, și fiecare manager apare la toate departamentele. Fără ON nu există asignarea actuală. Șirul nu a fost rulat în tur.
    39. Enunț: WHERE d.dept_name = 'Sales' OR 0 = 1 după un LEFT. De ce poate dispărea și ultimul manager fără departament?
        Capcană: 0 = 1 este fals, dar OR îl face opțional, deci NULL rămâne.
        Cheie: La pregătire, 0 = 1 se scoate. Rămâne WHERE d.dept_name = 'Sales', care este fals pe NULL. 10.2.1.9 poate rescrie LEFT ca inner. Managerul fără departament cade.
    40. Enunț: SELECT DISTINCT c1, c2 FROM t ORDER BY c3, iar c3 nu este în listă. De ce nu este o sortare de join?
        Capcană: Este ORDER BY obișnuit, iar DISTINCT doar înfrumusețează.
        Cheie: 14.19.3 respinge ORDER BY care nu este în lista lui DISTINCT, pentru că altfel păstrarea copiei ar fi arbitrară. Nu este o condiție de ON și nu spune ce tabelă s-a potrivit.

Alegere, inclusiv când rămâi în aplicație:
    41. Enunț: Întrebarea este „ce manager are departament și cum se numește”. Ce scrii?
        Capcană: LEFT JOIN, ca să nu pierzi pe nimeni, și ignori NULL la citire.
        Cheie: INNER JOIN ON dept_no. NULL nu trebuie să apară. LEFT ar păstra managerul fără nume și l-ai filtra din greșeală mai târziu cu un WHERE pe nume.
    42. Enunț: Întrebarea este „ce manager nu are rând în departments_dup”. Ce scrii?
        Capcană: INNER JOIN și WHERE d.dept_no IS NULL.
        Cheie: LEFT JOIN ON dept_no WHERE d.dept_no IS NULL, dacă cheia din dreapta este NOT NULL. NOT EXISTS dacă nu vrei să înmulțești stânga și nu ai nevoie de coloanele din dreapta.
    43. Enunț: Întrebarea este „fiecare manager, cu fiecare departament la care ar putea fi mutat”. Ce scrii?
        Capcană: INNER JOIN fără ON, pentru că în MySQL este același lucru și e mai scurt.
        Cheie: CROSS JOIN, produsul scris. JOIN fără ON este același produs în MySQL și nu spune că l-ai cerut. ON pe dept_no ar fi asignarea de azi, nu matricea.
    44. Enunț: Două liste de angajați trebuie una sub alta, iar omul care este în ambele rămâne de două ori. Ce scrii?
        Capcană: UNION, pentru că este rezultatul mai bun, sau INNER JOIN pe emp_no.
        Cheie: UNION ALL. UNION fără ALL scoate copia. JOIN ar pune coloanele pe același rând, nu una sub alta.
    45. Enunț: Media salariului trebuie lângă dept_name, iar omul poate avea mai multe titluri. Unde stă AVG?
        Capcană: După JOIN titles, ca să ai titlul și media în aceeași propoziție.
        Cheie: Într-o tabelă derivată grupată pe emp_no, înainte de join-ul care înmulțește. HAVING filtrează media. WHERE ar scoate salariul dinainte. ANY_VALUE nu repară înmulțirea.
    46. Enunț: Codul de departament există doar într-un serviciu din afara acestui MySQL. Unde faci potrivirea?
        Capcană: Într-un JOIN spre un șir JDBC, pentru că driverul este tot serverul.
        Cheie: În aplicație. Propoziția vede tabelele de pe acest server. Connector/J trimite șirul; nu aduce serviciul extern în ON. Efectul pe 3306 rămâne NERULAT în tur, pentru 1045.
    47. Enunț: Vrei departamentul actual și planul citește ambele tabele. Pui index sau schimbi INNER în LEFT?
        Capcană: LEFT, ca să ajute optimizerul să găsească mai repede.
        Cheie: Index pe coloana din ON, dacă măsurătoarea o cere. LEFT schimbă rândurile, nu doar planul. USE INDEX nu repară un ON greșit. Planul este alt folder.
    48. Enunț: Optimizerul alege o ordine proastă, dar perechea este corectă. STRAIGHT_JOIN sau un ON în plus?
        Capcană: Un ON în plus, ca să forțezi tabela mică.
        Cheie: STRAIGHT_JOIN dacă accepți că poate opri semijoin-ul și un index, cum spune 10.2.1.9. Un ON în plus schimbă perechea. Nu îl pune ca să repari ordinea.
    49. Enunț: Raportul nu trebuie să arate copia din employees_dup, dar copia trebuie să rămână până o verifică cineva. DISTINCT sau DELETE?
        Capcană: DELETE cu JOIN, ca să cureți, pentru că DISTINCT nu ține.
        Cheie: DISTINCT sau o listă fără COUNT, dacă vrei doar să taci copia. HAVING COUNT > 1 dacă vrei să o vezi. DELETE scoate rândul. UNIQUE oprește următorul INSERT. Nu este un join.
    50. Enunț: Un colet nu poate fi listat la raft dacă raftul nu există, coletul fără raft trebuie văzut separat, iar două liste de colete se lipesc păstrând dublura. Ce este fiecare?
        Capcană: Toate trei sunt INNER JOIN, fiindcă toate „leagă” date.
        Cheie: Perechea colet-raft este INNER JOIN. Coletul fără raft este LEFT JOIN și WHERE raftul este NULL. Lipirea listelor, cu dublura, este UNION ALL. Matricea tuturor rafturilor pentru fiecare colet ar fi CROSS, și nu este în întrebare.

🟢 Începător
    Ce este            banca de interviu pentru alegerea rândului care rămâne
    De ce e nevoie     fără cele cincizeci, INNER și LEFT rămân același cuvânt
🟡 Producție
    Cum se scrie       cincizeci de enunțuri în comentariu, fără main
🔴 Staff
    Unde               cheia stă aici. Proba care ține SQL-ul stă în clasele folderului. Această clasă nu o trimite.
Pași
    1. Citește enunțul.
    2. Cheia este deja scrisă, pentru artefact.
    3. Nu porni un main de recall.
*/
// Începător: banca stă lângă lecția scurtă și nu șterge un manager.
// Producție: package _10_JOINs.fast. Nu are main de recall.
// Expert: Cele cincizeci sunt despre alegerea îmbinării pe dept_manager și departments. Nu se lipesc pe PRIMARY KEY.
package _10_JOINs.fast;

// Începător: clasa ține întrebările și cheile în comentariul de deasupra.
// Producție: public final class Joins_Staff_Interview. Dacă primește main, nu mai este banca fără recall.
// Expert: Nu execută INNER JOIN-ul. Efectul pe server rămâne NERULAT în tur.
public final class Joins_Staff_Interview {
    // Începător: nu există o propoziție în corp.
    // Producție: acolada închide Joins_Staff_Interview. Sub ea nu se adaugă un main.
    // Expert: Cele cincizeci rămân în comentariu. Golul nu șterge o cheie.
}
