/*
TOPIC:
    Întrebări despre rândul care rămâne când alegi îmbinarea între dept_manager_dup și departments_dup.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să recunoști perechea, lipsa și produsul înainte să schimbi propoziția.
MECHANISM:
    Șirul din Joins.matchedManagerAndDepartment. Această clasă nu îl trimite.
LOOKALIKES:
    LEFT păstrează managerul cu NULL. JOIN fără ON este produsul în MySQL. UNION nu potrivește coloane.
BOOK THEORY:
    Manualul 9.7, 15.2.13.2: INNER fără pereche nu lasă rândul; LEFT umple dreapta cu NULL. 10.2.1.9: WHERE fals pe NULL poate face din LEFT un inner. Joins.pdf, paginile deschise, nu spune ce rând cade.
BOOK EXAMPLE:
    Joins.matchedManagerAndDepartment
REPO DEMO:
    src/main/java/_10_JOINs/JOINs.java
BREAK LAB:
    INNER JOIN și WHERE d.dept_no IS NULL, ținut în innerCannotListTheMissingDepartment.
TRADE-OFFS:
    LEFT când lipsa rămâne. UNION ALL când lipești liste. Aplicația când departamentul nu este pe acest server.
ANTI-PATTERNS:
    IS NULL după INNER, sau JOIN fără ON citit ca pereche.
STAFF QUESTIONS:
    Întrebările 1–50 din Joins_Staff_Interview. Aici sunt cinci, cu cheia scrisă.
NOVEL TRANSFER:
    Coletul cu raft, coletul fără raft, matricea colet-raft.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei îmbinări (join):
       Îmbinarea păstrează, din două referințe, rândurile pe care le cere forma scrisă: perechea, stânga cu NULL, sau produsul.

Exemplu:
       Joins_Terms.explainJoin()
       Managerul al cărui dept_no nu este în departments_dup nu apare după INNER JOIN.

1. Enunț: Ce problemă rezolvă INNER JOIN pe dept_no între manager și departament?
   Capcană: Aduce și managerul fără departament, cu numele gol.
   Cheie: Păstrează perechea. Managerul fără departments_dup cade din rezultat, nu din tabelă.
   Demonstrație: Joins.matchedManagerAndDepartment
2. Enunț: Ce rămâne în rezultat dacă dept_no al managerului nu este în departments_dup?
   Capcană: Rândul așteaptă COMMIT, apoi se șterge din dept_manager_dup.
   Cheie: Nu este în rezultatul lui INNER. Tabela rămâne. LEFT l-ar fi arătat, cu NULL la dreapta.
   Demonstrație: Joins.matchedManagerAndDepartment
3. Enunț: De ce IN (SELECT dept_no ...) nu este această îmbinare?
   Capcană: Aceiași manageri înseamnă același plan.
   Cheie: Subcererea nu aduce dept_name în același FROM. Același emp_no nu este același plan.
   Demonstrație: Joins.subqueryListsManagersByDepartmentList
4. Enunț: Ce se rupe dacă pui WHERE d.dept_no IS NULL după INNER?
   Capcană: Găsești managerul fără departament.
   Cheie: INNER l-a scos deja. IS NULL nu mai are rând NULL. Anti-join-ul este LEFT, cu IS NULL în WHERE.
   Demonstrație: Joins.innerCannotListTheMissingDepartment
5. Enunț: Când nu mai alegi INNER?
   Capcană: UNION este varianta mai bună a aceluiași join.
   Cheie: LEFT când lipsa trebuie să apară. CROSS când vrei fiecare cu fiecare. UNION ALL când lipești două rezultate și copia rămâne. Indexul nu schimbă alegerea.
   Demonstrație: Joins.matchedManagerAndDepartment

Întrebări adăugate:

*/
// Începător: întrebările scurte stau în acest folder, fără o metodă care să răspundă singură.
// Producție: package _10_JOINs.fast. Corpul clasei rămâne gol.
// Expert: Nu muta aici banca lui LEFT sau a lui UNION. Subiectul este alegerea rândului care rămâne.
package _10_JOINs.fast;

// Începător: clasa ține întrebările în comentariu și nu trimite nimic la server.
// Producție: public final class Joins_Questions. Dacă primește o metodă, nu mai este clasa goală cerută la creare.
// Expert: Cheia este scrisă deasupra. Nu este un main de recall și nu acoperă până la capăt AVG-ul înmulțit.
public final class Joins_Questions {
    // Începător: nu există o propoziție în corp, fiindcă răspunsurile stau în comentariu.
    // Producție: acolada închide Joins_Questions. Sub ea nu se adaugă un main.
    // Expert: matchedManagerAndDepartment rămâne în JOINs.java. Golul de aici nu șterge cele cinci întrebări.
}
