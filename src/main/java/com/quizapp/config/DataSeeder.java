package com.quizapp.config;

import com.quizapp.entity.Category;
import com.quizapp.entity.Question;
import com.quizapp.entity.Role;
import com.quizapp.entity.User;
import com.quizapp.entity.Level;
import com.quizapp.repository.CategoryRepository;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, CategoryRepository categoryRepository, QuestionRepository questionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            User student = new User();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setRole(Role.STUDENT);
            userRepository.save(student);
        }

        if (categoryRepository.count() == 0) {
            String[] catNames = {"DBMS", "OOPS", "SDN", "NPD", "Java Basics", "Web Development"};
            for (String cName : catNames) {
                Category cat = new Category();
                cat.setName(cName);
                cat.setDescription("Category for " + cName);
                cat = categoryRepository.save(cat);
                
                switch (cName) {
                    case "DBMS":
                        seedDBMS(cat);
                        break;
                    case "OOPS":
                        seedOOPS(cat);
                        break;
                    case "SDN":
                        seedSDN(cat);
                        break;
                    case "NPD":
                        seedNPD(cat);
                        break;
                    case "Java Basics":
                        seedJava(cat);
                        break;
                    case "Web Development":
                        seedWeb(cat);
                        break;
                }
            }
        }
    }

    private void addQuestions(Category cat, Level level, String[][] qData) {
        for (String[] data : qData) {
            Question q = new Question();
            q.setCategory(cat);
            q.setLevel(level);
            q.setText(data[0]);
            q.setOptionA(data[1]);
            q.setOptionB(data[2]);
            q.setOptionC(data[3]);
            q.setOptionD(data[4]);
            q.setCorrectOption(data[5]);
            questionRepository.save(q);
        }
    }

    private void seedDBMS(Category cat) {
        String[][] basics = {
            {"What is normalization in DBMS?", "A technique to speed up queries", "Method to increase data duplication", "Organizing data to reduce redundancy", "A way to delete tables", "C"},
            {"Which is not a valid SQL command?", "UPDATE", "REMOVE", "DELETE", "TRUNCATE", "B"},
            {"What does ACID stand for?", "Atomicity, Consistency, Isolation, Durability", "Accuracy, Completeness, Independence, Data", "Allocation, Concurrency, Indexing, Deletion", "Array, Column, Integer, Date", "A"},
            {"Which key uniquely identifies each row?", "Foreign Key", "Candidate Key", "Super Key", "Primary Key", "D"},
            {"ER model is based on?", "Trees and graphs", "Entities and relationships", "Hashes and pointers", "Spreadsheets and rows", "B"}
        };
        addQuestions(cat, Level.BASICS, basics);

        String[][] medium = {
            {"What does DDL stand for?", "Data Definition Language", "Data Descriptive Language", "Document Definition Language", "Dynamic Data Language", "A"},
            {"Which of the following is an aggregate function?", "LIKE", "COUNT", "HAVING", "GROUP BY", "B"},
            {"A relation is in 2NF if?", "It has no composite key", "It is in 1NF and no partial dependencies exist", "It has no multi-valued attributes", "All attributes are atomic", "B"},
            {"Which command is used to remove a table?", "DELETE", "DROP", "REMOVE", "TRUNCATE", "B"},
            {"What is a View in SQL?", "A physical table", "A virtual table based on the result of a query", "An index on a table", "A trigger", "B"},
            {"What is a dead-lock in DBMS?", "Infinite loop", "Concurrency anomaly", "Two transactions waiting on each other indefinitely", "Data loss issue", "C"},
            {"What is the purpose of the COMMIT statement?", "Rollback changes", "Make changes permanent", "End the current session", "Start a new transaction", "B"},
            {"Which level of normalization eliminates transitive dependencies?", "1NF", "2NF", "3NF", "BCNF", "C"},
            {"What does a Foreign Key guarantee?", "Referential Integrity", "Entity Integrity", "Domain Integrity", "Operational Integrity", "A"},
            {"Which of the following joins returns all rows from both tables?", "INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL OUTER JOIN", "D"}
        };
        addQuestions(cat, Level.MEDIUM, medium);

        String[][] advanced = {
            {"What is the difference between TRUNCATE and DELETE?", "DELETE logs each row deletion, TRUNCATE doesn't", "TRUNCATE can be rolled back, DELETE cannot", "DELETE is a DDL command, TRUNCATE is DML", "They are identical", "A"},
            {"What is BCNF?", "Boyce-Codd Normal Form", "Basic Codd Normal Form", "Binary Coded Normal Form", "Business Common Normal Form", "A"},
            {"In B+ Trees, leaf nodes are linked via?", "Hash pointers", "Linked Lists", "Arrays", "Graphs", "B"},
            {"What is the Two-Phase Locking (2PL) protocol?", "Ensures serializability", "Prevents deadlocks completely", "Only used in distributed databases", "Guarantees no starvation", "A"},
            {"What is a cluster index in DBMS?", "An index that defines the physical storage order", "A secondary index", "An index spanning multiple tables", "An index for clustered computing", "A"},
            {"What does the WAL (Write-Ahead Logging) protocol ensure?", "Data remains consistent upon crash", "Faster reads", "Eliminates need for locks", "Replaces 2PL", "A"},
            {"MVD (Multi-Valued Dependency) is addressed in which Normal Form?", "3NF", "BCNF", "4NF", "5NF", "C"},
            {"What is a materialized view?", "A view that caches the result set physically", "A standard SQL virtual view", "A view visible to all users", "A view with dynamic binding", "A"},
            {"What does REDO do in transaction recovery?", "Reapplies changes of committed transactions", "Undoes changes of uncommitted transactions", "Forces a flush to disk", "Restarts the database", "A"},
            {"What is Phantom Read anomaly?", "Reading uncommitted data", "A row appears/disappears between two identical queries in different transactions", "Reading a modified value that gets rolled back", "Losing a concurrent update", "B"},
            {"Which isolation level prevents Phantom Reads?", "READ UNCOMMITTED", "READ COMMITTED", "REPEATABLE READ", "SERIALIZABLE", "D"},
            {"What is RAFT?", "An SQL query optimizer", "A distributed consensus algorithm", "A storage engine for MySQL", "A buffer management strategy", "B"},
            {"CAP Theorem states a distributed system can only provide 2 out of 3: Consistency, Availability, and?", "Performance", "Partition Tolerance", "Persistence", "Parallelism", "B"},
            {"A dirty read occurs when?", "A transaction reads uncommitted data of another transaction", "The disk sector is corrupted", "Database hasn't been vacuumed", "Data is flushed prematurely", "A"},
            {"What is cost-based optimization in SQL?", "Choosing execution plan based on estimated resource cost", "Billing optimization for cloud DBs", "Removing unused indexes", "Compressing column data", "A"}
        };
        addQuestions(cat, Level.ADVANCED, advanced);
    }

    private void seedOOPS(Category cat) {
        String[][] basics = {
            {"Which is not a pillar of Object-Oriented Programming?", "Polymorphism", "Inheritance", "Compilation", "Encapsulation", "C"},
            {"What does 'Encapsulation' refer to?", "Hiding the implementation details from the user", "Binding data and methods together", "Calling a method inside itself", "Deriving a new class from an existing one", "B"},
            {"Which feature allows a function to behave differently depending on arguments?", "Overriding", "Polymorphism", "Abstraction", "Encapsulation", "B"},
            {"In OOP, what is a 'Class'?", "A blueprint from which objects are created", "A specific instance of an object", "A variable holding primitive data", "A reserved keyword strictly for loops", "A"},
            {"Which concept allows a child class to acquire properties from parent?", "Inheritance", "Abstraction", "Encapsulation", "Polymorphism", "A"}
        };
        addQuestions(cat, Level.BASICS, basics);

        String[][] medium = {
            {"What is Abstraction?", "Hiding internal complexity and showing only essential features", "Creating multiple instances", "Connecting database to models", "Restricting memory access", "A"},
            {"Which type of inheritance is generally NOT supported in Java (for classes)?", "Single", "Multilevel", "Hierarchical", "Multiple", "D"},
            {"What is method overriding?", "Having multiple methods with same name but different parameters", "Subclass providing specific implementation of a superclass method", "Calling a method recursively", "Blocking a method call", "B"},
            {"What is method overloading?", "Multiple methods having same name but different parameters", "A subclass modifying a parent method", "Calling a method too many times", "Allocating variables dynamically", "A"},
            {"What is an interface?", "A completely abstract class containing only abstract methods (in typical OOP)", "A variable declaration", "A graphical user screen", "A physical cable connection", "A"},
            {"What keyword is used to stop method overriding?", "static", "final", "abstract", "private", "B"},
            {"Can we instantiate an abstract class?", "Yes", "No", "Only if it has no methods", "Only through reflection", "B"},
            {"What does the 'super' keyword do?", "Refers to parent class objects", "Calls a static method", "Terminates the program", "Declares an interface", "A"},
            {"What is a constructor?", "A method to delete objects", "A special method called when an object is created", "A primitive data type", "An error handling mechanism", "B"},
            {"Which access modifier limits visibility to the same class?", "public", "protected", "private", "default", "C"}
        };
        addQuestions(cat, Level.MEDIUM, medium);

        String[][] advanced = {
            {"What does SOLID stand for?", "Single responsibility, Open-closed, Liskov substitution, Interface segregation, Dependency inversion", "Static Object Logic In Database", "Safe Object Link In Design", "Simple Overload Logic Internal Data", "A"},
            {"What is early binding?", "Method call is resolved at runtime", "Method call is resolved at compile time", "Connecting to a database before app starts", "Loading UI early", "B"},
            {"What is late binding mostly associated with?", "Method overloading", "Method overriding", "Static classes", "Constructors", "B"},
            {"How does composition differ from inheritance?", "Composition is a 'has-a' relationship, inheritance is an 'is-a' relationship", "Composition scales worse", "Inheritance allows multiple parents", "Composition is only for primitive types", "A"},
            {"What does the Liskov Substitution Principle state?", "Subtypes must be substitutable for their base types", "Interfaces must be small", "Dependencies should point to abstractions", "A class should have one reason to change", "A"},
            {"What is an Association in OOP?", "A relationship where all objects have their own lifecycle", "A strict parent-child relation", "An inheritance tree", "A memory footprint concept", "A"},
            {"What defines an Aggregation?", "A specialized form of Association where a child can exist independently of the parent", "A strict composition", "An abstract method", "A global variable", "A"},
            {"What is runtime polymorphism also known as?", "Static Method Dispatch", "Dynamic Method Dispatch", "Message Passing", "Encapsulation", "B"},
            {"What is covariance in OOP?", "Ability to override a method to return a narrower type", "Converting integer to float", "Variables moving together in memory", "Throwing the same exceptions", "A"},
            {"Why favor composition over inheritance?", "It provides better encapsulation and flexibility at runtime", "It runs faster", "It uses less memory", "It allows multiple inheritance directly", "A"},
            {"What is a pure virtual function in C++ terms?", "An abstract method with no implementation in the base class", "A method that returns true", "A static block", "An overridden constructor", "A"},
            {"What happens if a child class doesn't implement all abstract methods of its parent?", "It automatically gets empty bodies", "It must also be declared abstract", "It compiles normally", "It throws ArithmeticException", "B"},
            {"Can a static method be overridden?", "Yes, it is dynamically bound", "No, it is hidden by the subclass method", "Yes, but only in interfaces", "No, it throws an error", "B"},
            {"What does the 'open-closed principle' dictate?", "Classes should be open for extension but closed for modification", "Source code should be open-source", "Ports should be open", "Methods should be open to overrides", "A"},
            {"What is downcasting?", "Assigning a superclass reference to a subclass type", "Converting a double to an int", "Lowering visibility of a variable", "Throwing exceptions downwards", "A"}
        };
        addQuestions(cat, Level.ADVANCED, advanced);
    }

    private void seedSDN(Category cat) {
        String[][] basics = {
            {"What does SDN stand for?", "Standard Data Network", "Software-Defined Networking", "Secure Digital Node", "System Distribution Network", "B"},
            {"Which two planes are separated in SDN?", "Control and Data Plane", "User and Root Plane", "Access and Core Plane", "Physical and Logical Plane", "A"},
            {"Which protocol is most commonly associated with SDN southbound interfaces?", "BGP", "OSPF", "OpenFlow", "HTTP", "C"},
            {"What is the primary role of the SDN Controller?", "Store large databases", "Forward packets physically", "Centralize network control logic", "Encrypt signals", "C"},
            {"Primary benefit of SDN?", "Eliminates physical cables", "Network programmability and agility", "Requires no power", "Automatic website generation", "B"}
        };
        addQuestions(cat, Level.BASICS, basics);

        String[][] medium = {
            {"What does the Southbound API connect?", "Controller to the network devices", "Applications to the controller", "Users to applications", "Switches to end hosts", "A"},
            {"What does the Northbound API connect?", "Controller to the network devices", "SDN applications to the controller", "Two different controllers", "Router to Router", "B"},
            {"Which of the following is an open-source SDN controller?", "Cisco ACI", "OpenDaylight", "VMware NSX", "Juniper Contrail", "B"},
            {"What is OpenFlow?", "An IP routing protocol", "A communications protocol for the control plane to interact with the forwarding plane", "A proprietary security standard", "A cloud orchestrator", "B"},
            {"In a traditional network, where does the control plane reside?", "In a central server", "Distributed across all network devices", "In the cloud", "In personal computers", "B"},
            {"What action does a switch take in OpenFlow if it has no match for a flow?", "Drops it automatically", "Broadcasts it to all ports", "Sends the packet to the SDN Controller", "Reboots", "C"},
            {"Which table does the forwarding plane use to move packets?", "Flow Table", "ARP Table", "Control Table", "MAC Table", "A"},
            {"What is the East-West API used for in SDN?", "Connecting applications to users", "Communication between multiple SDN controllers", "Connecting to legacy switches", "Routing between datacenters", "B"},
            {"What does NFV stand for?", "Node Forwarding Vector", "Network Functions Virtualization", "Neutral Framework Variable", "New Fiber Velocity", "B"},
            {"How does SDN improve network security?", "By replacing firewalls", "By centralizing policy enforcement and traffic monitoring", "By encrypting all cables", "By turning off unused ports automatically", "B"}
        };
        addQuestions(cat, Level.MEDIUM, medium);

        String[][] advanced = {
            {"What represents a flow entry in an OpenFlow switch?", "Match Fields, Priority, Counters, Instructions, Timeouts", "IP Address, MAC Address, Port", "VLAN tag, Source IP", "Destination MAC, Drop policy", "A"},
            {"What is Open vSwitch (OVS)?", "A hardware router", "A multilayer virtual switch often used in virtualized environments", "A proprietary Cisco platform", "An SDN debugging tool", "B"},
            {"Which mechanism helps prevent a single point of failure in SDN?", "Controller Clustering", "Removing the Southbound API", "Decreasing OpenFlow versions", "Increasing cable redundancy", "A"},
            {"What is 'In-band' control in SDN?", "Control traffic shares the same data path as user traffic", "Control traffic has a dedicated physical network", "No control traffic is allowed", "All control traffic is encrypted", "A"},
            {"Which version of OpenFlow introduced Group Tables?", "1.0", "1.1", "1.3", "1.5", "B"},
            {"In SDN, what is a reactive flow setup?", "Controller pushes flows before traffic arrives", "Switch queries controller when it sees a new, unmatched packet", "Traffic is routed without flow rules", "Network heals itself automatically", "B"},
            {"What is proactive flow setup?", "Controller pushes flow entries to switches beforehand", "Switch asks for rules per packet", "Flows are loaded from deep storage", "Users configure their own routers", "A"},
            {"What problem does 'Packet-In' storm cause?", "Overwhelms the SDN Controller with queries", "Crashes the switch hardware", "Destroys fiber optic cables", "Increases database storage needs", "A"},
            {"How does segment routing integrate with SDN?", "It removes the need for controllers", "It uses source routing to steer packets without maintaining per-flow state at every node", "It encrypts segment edges", "It limits the MTU size", "B"},
            {"What is Mininet?", "A mini router", "An SDN network emulator building virtual networks on a single machine", "A lightweight controller", "A tiny hardware switch", "B"},
            {"What does the P4 language do for networking?", "It predicts packet errors", "It programs the data plane forwarding behavior of a switch hardware/software", "It provides a web interface for controllers", "It is an alternative to Python", "B"},
            {"What does RESTCONF provide in SDN?", "A programmable interface using HTTP/REST patterns to access YANG datastores", "A replacement for OpenFlow", "A database system", "A GUI for switches", "A"},
            {"How do intent-based networks (IBN) differ from standard SDN?", "No difference, they are synonyms", "IBN translates high-level business goals into network configurations automatically", "IBN removes controllers", "IBN uses older hardware", "B"},
            {"What is the 'Split Architecture' concept?", "Splitting optical paths", "Decoupling control logic from forwarding hardware", "Using dual CPUs in switches", "Dividing data centers physically", "B"},
            {"What is a TTP (Table Typing Pattern) in OpenFlow?", "An optical pattern", "A specification to describe the logical table structures a switch hardware supports", "A protocol to format text", "A way to organize passwords", "B"}
        };
        addQuestions(cat, Level.ADVANCED, advanced);
    }

    private void seedNPD(Category cat) {
        String[][] basics = {
            {"What does NPD stand for in business?", "Network Processing Device", "New Product Development", "National Public Database", "Nominal Point Distribution", "B"},
            {"Which is usually the first stage in the NPD process?", "Idea Generation", "Commercialization", "Prototyping", "Market Testing", "A"},
            {"What is a 'Prototype'?", "The final marketed product", "A legal patent application", "An early sample or model", "Advertising campaign", "C"},
            {"Why is Concept Testing important?", "Check factory mass production", "Evaluate consumer response before building", "Fix bugs", "Not important", "B"},
            {"Which stage comes just before full product launch?", "Idea Screening", "Brainstorming", "Market Testing", "Business Analysis", "C"}
        };
        addQuestions(cat, Level.BASICS, basics);

        String[][] medium = {
            {"What occurs during Idea Screening?", "Selling the product to distributors", "Evaluating ideas to drop poor ones", "Building the first unit", "Running a social media campaign", "B"},
            {"What is Business Analysis in NPD?", "Filing for bankruptcy", "Reviewing sales, costs, and profit projections", "Analyzing competitor's source code", "Hiring new staff", "B"},
            {"What is Product Development in the NPD cycle?", "Brainstorming ideas", "Converting the concept into a physical product or software", "Publishing advertisements", "Gathering customer surveys", "B"},
            {"What is the primary goal of Commercialization?", "Introducing the new product into the market", "Scrapping unused ideas", "Testing the product in a lab", "Training the sales team internally", "A"},
            {"Who are 'Early Adopters'?", "People who never buy products", "Customers who adopt a new product very early in its lifecycle", "Competitor analysis teams", "Late-stage investors", "B"},
            {"What is a Minimum Viable Product (MVP)?", "A product with maximum features", "A product with enough features to attract early-adopter customers and validate a product idea", "A failed prototype", "A fully completed business plan", "B"},
            {"Why might a company use Crowdsourcing in NPD?", "To gather ideas or funds from a large group of people via the internet", "To physically crowd stores", "To build servers", "To avoid paying taxes", "A"},
            {"What is the Stage-Gate process?", "A method to restrict employee moving", "A project management technique dividing a project into stages separated by gates", "A way to ship products via gates", "A financial audit method", "B"},
            {"What is 'Time to Market' (TTM)?", "The time a store stays open", "The length of time from product conception to commercialization", "The transport time for shipping", "The marketing campaign duration", "B"},
            {"Why do many new products fail?", "Poor market research and lack of consumer need", "Too much funding", "They are too advanced", "They lack bugs", "A"}
        };
        addQuestions(cat, Level.MEDIUM, medium);

        String[][] advanced = {
            {"What is QFD (Quality Function Deployment)?", "A structured approach to defining customer needs and translating them into specific plans", "A rapid prototyping tool", "A QA testing framework for software", "A factory layout plan", "A"},
            {"What is the Kano Model used for in NPD?", "Predicting financial collapse", "Classifying customer preferences into categories like Basic, Performance, and Excitement", "Creating 3D models", "Calculating shipping routes", "B"},
            {"What defines 'Disruptive Innovation'?", "A new technology that completely displaces an established market or technology", "A broken product", "A slight improvement in battery life", "An overpriced luxury item", "A"},
            {"What is 'Value Engineering'?", "Maximizing profit by raising prices", "Systematic method to improve the 'value' of goods by examining function and cost", "Engineering marketing materials", "Designing the supply chain", "B"},
            {"How does Agile Methodology apply to NPD?", "It replaces all planning with random tasks", "It relies on iterative development, cross-functional teams, and continuous feedback", "It ensures rigid phase sequences", "It minimizes team communication", "B"},
            {"What is Conjoint Analysis in market research?", "Joining two companies", "A statistical technique used to evaluate how people value different attributes of a product", "Analyzing joint accounts", "Testing physical joints of a hardware product", "B"},
            {"What does the term 'Cannibalization' refer to in product lines?", "When a new product eats into the sales of the company's existing products", "When competitors steal ideas", "When a product gets recalled", "When production costs consume profits", "A"},
            {"What is 'Design for Manufacturability' (DFM)?", "Designing a product so it is easy and cost-effective to manufacture", "Designing marketing banners", "Designing a software architecture", "A legal framework for manufacturing", "A"},
            {"What is the 'Chasm' in the Technology Adoption Life Cycle?", "A deep hole in factories", "The gap between visionary early adopters and the pragmatic early majority", "The time gap between ideation and prototyping", "The financial gap when funding runs out", "B"},
            {"What is 'Open Innovation'?", "A business adopting open-source software", "Using purposive inflows and outflows of knowledge to accelerate internal innovation", "An open-door office policy", "Leaving trade secrets public", "B"},
            {"What is an innovation 'S-Curve'?", "A graph showing product performance over time relative to engineering effort", "The curve on a mouse", "A sales chart", "A supply chain pipeline", "A"},
            {"What is Lean Startup methodology?", "Starting a company with no desks", "Developing businesses using validated learning, experiments, and iterative releases", "Cutting employee salaries to save money", "Creating extremely simple products", "B"},
            {"What does TRIZ signify in problem solving?", "A Russian acronym for 'Theory of Inventive Problem Solving'", "A three-phase gating mechanism", "A prototyping machine", "A market analysis algorithm", "A"},
            {"What is a 'Blue Ocean Strategy'?", "Fishing in the ocean", "Simultaneous pursuit of differentiation and low cost to open a new, uncontested market space", "Copying competitors blindly", "Lowering prices dramatically", "B"},
            {"How does a 'Platform Strategy' benefit NPD?", "Selling actual platforms", "Allowing multiple products or services to be built upon a robust core architecture", "Creating flat organizational charts", "Basing products entirely on external software", "B"}
        };
        addQuestions(cat, Level.ADVANCED, advanced);
    }

    private void seedJava(Category cat) {
        String[][] basics = {
            {"Which creates a new object in Java?", "new", "create", "alloc", "construct", "A"},
            {"What is the entry point of a Java app?", "public start()", "main() method", "init() method", "The constructor", "B"},
            {"Is Java strongly or loosely typed?", "Loosely Typed", "Not Typed", "Strongly Typed", "Dynamically Typed", "C"},
            {"Which primitive type takes 8 bytes?", "int", "float", "char", "double", "D"},
            {"Which exception when dividing by zero?", "NullPointerException", "ArithmeticException", "IOException", "ClassNotFoundException", "B"}
        };
        addQuestions(cat, Level.BASICS, basics);

        String[][] medium = {
            {"Which of these cannot be used as a variable name in Java?", "identifier", "keyword", "volatile", "Both keyword and volatile", "D"},
            {"What is the size of boolean variable?", "8 bit", "16 bit", "32 bit", "Not precisely defined by specification", "D"},
            {"Which keyword is used to access features of a package?", "package", "import", "extends", "export", "B"},
            {"What is the default value of a local variable?", "0", "null", "No default value, must be initialized", "false", "C"},
            {"String in Java is a?", "Primitive Data Type", "Wrapper Class", "Object/Reference Type", "Enum", "C"},
            {"What is the Superclass of all classes in Java?", "System", "Class", "Object", "Main", "C"},
            {"Which method must be implemented by all threads?", "start()", "stop()", "run()", "execute()", "C"},
            {"Is an array an object in Java?", "No", "Yes", "Only if it contains objects", "Only in Java 8+", "B"},
            {"What does the 'final' keyword do to a variable?", "Makes it static", "Makes it unchangeable (constant)", "Makes it global", "Makes it accessible everywhere", "B"},
            {"What is a Wrapper class?", "A UI container class", "A class that encapsulates a primitive type", "A security wrapper", "A database wrapper", "B"}
        };
        addQuestions(cat, Level.MEDIUM, medium);

        String[][] advanced = {
            {"What is a memory leak in Java?", "When Java's garbage collector accidentally clears memory", "When objects are no longer used but still referenced, preventing GC", "When the heap space grows completely random", "C/C++ pointers corrupting memory", "B"},
            {"What does the 'volatile' keyword guarantee in Java?", "Atomicity of compound operations", "Visibility of thread modifications to other threads", "Variable cannot be modified", "Thread sleep mechanisms", "B"},
            {"What is the Fork/Join framework used for?", "Joining strings efficiently", "Parallel execution of tasks using work-stealing algorithms", "Database joins", "Creating a new process fork", "B"},
            {"Difference between Callable and Runnable?", "Callable can return a result and throw a checked exception", "Runnable is for GUI, Callable for backend", "Runnable returns a boolean", "Callable is deprecated", "A"},
            {"What is Type Erasure in Java Generics?", "Compiler replaces generic types with Object or bound type at compile time", "Erasing data from memory", "Formatting text heavily", "A bug in Java 7", "A"},
            {"How does a ReentrantLock differ from an intrinsic lock (synchronized)?", "It is identical", "Provides advanced features like fairness, timed lock waits, and interruptibility", "It cannot be unlocked", "It works only for single threads", "B"},
            {"What does the 'transient' keyword do?", "Makes a process run faster", "Prevents a field from being serialized", "Hides the field from reflection", "Synchronizes the variable", "B"},
            {"What does String.intern() do?", "Interns a student", "Returns a canonical representation of the string from the string pool", "Converts string to int", "Encrypts the string", "B"},
            {"What is a ThreadLocal variable?", "A variable scoped to a specific thread", "A local variable inside run() method", "A variable shared across all threads", "A thread's name", "A"},
            {"What is Double-Checked Locking used for?", "Securing databases", "Safely implementing the Singleton pattern in a multithreaded environment", "Checking user passwords twice", "Locking two threads at once", "B"},
            {"Which GC algorithm is the default in Java 11/17+?", "Serial GC", "Parallel GC", "G1 (Garbage-First) GC", "ZGC", "C"},
            {"What is an invokedynamic instruction?", "A JDBC command", "A JVM instruction for dynamic method invocation (often used for lambdas)", "A way to call Python from Java", "A reflection method", "B"},
            {"What does the CompletableFuture class provide?", "A future that can be explicitly completed, enabling asynchronous programming", "A timeline for execution", "A replacement for standard loops", "A way to complete application exits", "A"},
            {"What is the purpose of the 'strictfp' keyword?", "Ensures floating-point operations are strictly compliant with IEEE 754 standards", "Makes floating point math extremely fast", "Declares a variable strict", "It restricts access to a file", "A"},
            {"How does functional interface in Java 8 differ from a regular interface?", "It must extend Object", "It can only have exactly one abstract method", "It cannot have static methods", "It cannot have default methods", "B"}
        };
        addQuestions(cat, Level.ADVANCED, advanced);
    }

    private void seedWeb(Category cat) {
        String[][] basics = {
            {"What does HTML stand for?", "Hyper Text Multiple Language", "Hyper Text Markup Language", "Home Text Markup Language", "High Text Moderation Language", "B"},
            {"Which technology manages styling?", "HTML", "JavaScript", "CSS", "Python", "C"},
            {"CSS property to change background color?", "color", "bgcolor", "background-color", "background-style", "C"},
            {"Which HTML tag creates a hyperlink?", "<a>", "<link>", "<href>", "<url>", "A"},
            {"JS code goes inside which HTML tags?", "<javascript>", "<script>", "<js>", "<code>", "B"}
        };
        addQuestions(cat, Level.BASICS, basics);

        String[][] medium = {
            {"What does CSS stand for?", "Cascading Style Sheets", "Creative Style System", "Computer Style Sheets", "Colorful Style Sheets", "A"},
            {"In CSS, what does 'box-sizing: border-box' do?", "Turns a box into a border", "Includes padding and border in the element's total width and height", "Removes borders entirely", "Makes boxes float", "B"},
            {"Which JavaScript method is used to select an element by ID?", "getElementById()", "getElementByClass()", "querySelector('#') [with hash]", "Both A and C are correct", "D"},
            {"What is the purpose of the 'alt' attribute in an <img> tag?", "To style the image", "To provide alternative text if the image fails to load", "To make it an alternate link", "To align the image", "B"},
            {"What does a REST API usually return?", "HTML documents", "CSS files", "JSON or XML data", "A database table", "C"},
            {"What is 'Local Storage' in a browser?", "A place to upload files", "A web storage API that stores data with no expiration time", "A hidden folder on desktop", "A synonym for cookies", "B"},
            {"What does 'responsive web design' mean?", "A site that responds quickly to clicks", "A site that dynamically adapts to different screen sizes", "A site that answers user questions", "A site with lots of animations", "B"},
            {"What does the 'npm' command do?", "Node Package Manager - manages JavaScript dependencies", "New Project Manager", "Network Protocol Monitor", "No Problem Maker", "A"},
            {"Which HTTP method is typically used to create a new resource?", "GET", "PUT", "POST", "DELETE", "C"},
            {"What is CORS?", "Cross-Origin Resource Sharing", "Computer Origin Request System", "Cascading Origin Routing Sheet", "Central Online Resource Server", "A"}
        };
        addQuestions(cat, Level.MEDIUM, medium);

        String[][] advanced = {
            {"What is the 'Virtual DOM' in React?", "A heavy 3D DOM", "A lightweight, in-memory representation of the real DOM, used to optimize updates", "A completely different DOM provided by browsers", "A term for shadow DOM", "B"},
            {"What does the 'event delegation' pattern involve?", "Delegating events to Web Workers", "Attaching a single event listener to a parent element to manage events for its children", "Using only one event across the whole app", "Removing all event listeners", "B"},
            {"In CSS Grid, what does 'fr' stand for?", "Frame Ratio", "Fractional Unit", "Flexible Row", "Free Region", "B"},
            {"What is the primary difference between cookie and localStorage?", "Cookies are sent to server with every HTTP request, localStorage is client-side only", "Cookies hold more data (up to 50MB)", "localStorage expires after session closes", "There is no difference", "A"},
            {"What does the 'defer' attribute on a <script> tag do?", "Downloads script in background and executes after HTML is completely parsed", "Stops the script from running entirely", "Deletes the script", "Executes script immediately blocking parsing", "A"},
            {"What is SSR (Server-Side Rendering)?", "Rendering the HTML on the server before sending to the client", "Rendering JS heavily on the client", "Sending plain text to the server", "Streaming video from the server", "A"},
            {"What is a Service Worker used for?", "To fix hardware services", "To enable background sync, push notifications, and offline caching", "To serve database directly", "To style a page", "B"},
            {"What is an IIFE in JavaScript?", "Immediately Invoked Function Expression", "Internal Iteration For Execution", "Inline Interface For Elements", "Immediate Instance Fixation Engine", "A"},
            {"In a Promises chain, what does .catch() handle?", "Only network errors", "Exceptions or rejections from the preceding Promises", "Catching a ball animation", "Stopping the event loop", "B"},
            {"What does HTTP/2 bring over HTTP/1.1?", "Multiplexing multiple requests over a single TCP connection", "Removal of headers", "Plain text only transmission", "UDP support replacing TCP", "A"},
            {"What is the BEM methodology in CSS?", "Block Element Modifier, a naming convention for classes", "Best Execution Method", "Background Effect Motion", "Bold Emphasis Modifier", "A"},
            {"What does 'hoisting' mean in JavaScript?", "Lifting heavy objects", "Variable and function declarations are physically moved to the top of the file", "Declarations are conceptually moved to the top of their scope before execution", "Deleting unused variables automatically", "C"},
            {"What's the difference between == and === in JavaScript?", "== checks type + value, === checks only value", "Both do the exact same thing", "== checks value with type coercion, === checks value and type without coercion", "=== is just older syntax", "C"},
            {"What is meant by 'Critical Rendering Path'?", "A bug in rendering engines", "The sequence of steps the browser goes through to convert HTML/CSS/JS into pixels on the screen", "The path an image follows to download", "A hardware fault", "B"},
            {"What's the purpose of WebAssembly (Wasm)?", "Replacing JavaScript completely", "Allowing code written in languages like C/C++/Rust to run at near-native speed in the browser", "A framework for building mobile apps", "An assembly language for databases", "B"}
        };
        addQuestions(cat, Level.ADVANCED, advanced);
    }
}
