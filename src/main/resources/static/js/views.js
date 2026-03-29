const views = {
    login: `
        <div class="glass-card" style="max-width: 400px; margin: 0 auto; text-align: center;">
            <h2>Login to <span class="highlight">QuizApp</span></h2>
            <form id="loginForm" class="mt-20 text-left">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" id="username" class="form-control" required />
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" id="password" class="form-control" required />
                </div>
                <button type="submit" class="btn btn-primary" style="width: 100%">Login</button>
            </form>
            <p class="mt-20">Don't have an account? <a href="#/register" class="highlight">Register</a></p>
        </div>
    `,
    register: `
        <div class="glass-card" style="max-width: 400px; margin: 0 auto; text-align: center;">
            <h2>Create an <span class="highlight">Account</span></h2>
            <form id="registerForm" class="mt-20 text-left">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" id="regUsername" class="form-control" required />
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" id="regPassword" class="form-control" required />
                </div>
                <div class="form-group">
                    <label>Role</label>
                    <select id="regRole" class="form-control">
                        <option value="STUDENT">Student</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-secondary" style="width: 100%">Register</button>
            </form>
            <p class="mt-20">Already have an account? <a href="#/login" class="highlight">Login</a></p>
        </div>
    `,
    studentDashboard: `
        <div>
            <h2>Welcome to your <span class="highlight">Dashboard</span></h2>
            <div class="glass-card mt-20">
                <h3>Available Categories</h3>
                <div id="categoryGrid" class="dashboard-grid">
                    <!-- Categories will be injected here -->
                    Loading...
                </div>
            </div>
            
            <div class="glass-card mt-40">
                <h3>Your Recent Attempts</h3>
                <div class="table-wrapper">
                    <table id="attemptsTable">
                        <thead>
                            <tr>
                                <th>Category</th>
                                <th>Score</th>
                                <th>Total</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Attempts injected here -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    `,
    levelSelect: `
        <div class="glass-card text-center" style="max-width: 600px; margin: 0 auto;">
            <h2>Choose <span class="highlight">Difficulty</span></h2>
            <p class="mt-20">Select a difficulty level to start the quiz.</p>
            <div class="mt-40 flex" style="gap: 20px; justify-content: center;">
                <button class="btn btn-secondary" id="btnBasics">Basics (5 Qs)</button>
                <button class="btn btn-primary" id="btnMedium">Medium (10 Qs)</button>
                <button class="btn btn-danger" id="btnAdvanced">Advanced (15 Qs)</button>
            </div>
            <div class="mt-40"><a href="#/dashboard" style="color: var(--text-color);">Back to Dashboard</a></div>
        </div>
    `,
    quiz: `
        <div class="glass-card" style="max-width: 800px; margin: 0 auto;">
            <div class="quiz-header">
                <h2 id="quizCategoryName">Loading...</h2>
                <div class="timer" id="quizTimer">05:00</div>
            </div>
            
            <div id="questionContainer">
                <h3 id="questionText">Preparing your questions...</h3>
                <form id="quizForm" class="mt-20">
                    <div id="optionsContainer"></div>
                </form>
            </div>
            
            <div class="flex justify-between mt-40" style="align-items: center;">
                <span id="questionProgress"></span>
                <div style="margin-left: auto;">
                    <button class="btn btn-secondary" id="submitAnswerBtn" style="display:none; margin-right: 10px;">Submit Answer</button>
                    <button class="btn btn-primary" id="nextBtn" style="display:none">Next Question</button>
                    <button class="btn btn-secondary" id="submitQuizBtn" style="display:none">Submit Quiz</button>
                </div>
            </div>
        </div>
    `,
    results: `
        <div class="glass-card text-center" style="max-width: 800px; margin: 0 auto;">
            <h2>Quiz <span class="highlight">Results</span></h2>
            <div class="mt-40" style="font-size: 3rem; font-weight: 700;">
                <span id="resultScore" class="highlight">0</span> / <span id="resultTotal">0</span>
            </div>
            <p class="mt-20">You have completed the quiz!</p>
            <div class="mt-40 flex" style="gap: 20px; justify-content: center;">
                <a href="#/dashboard" class="btn btn-primary">Back to Dashboard</a>
                <a href="#/leaderboard" class="btn btn-secondary">View Leaderboard</a>
            </div>
            <div id="reviewContainer" class="mt-40 text-left"></div>
        </div>
    `,
    leaderboard: `
        <div class="glass-card">
            <h2 class="text-center">Global <span class="highlight">Leaderboard</span></h2>
            <div class="table-wrapper">
                <table id="leaderboardTable">
                    <thead>
                        <tr>
                            <th>Rank</th>
                            <th>Student</th>
                            <th>Category</th>
                            <th>Score</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!-- Leaderboard injected here -->
                    </tbody>
                </table>
            </div>
        </div>
    `,
    adminDashboard: `
        <div>
            <h2>Admin <span class="highlight">Panel</span></h2>
            
            <div class="dashboard-grid">
                <div class="glass-card">
                    <h3>Categories</h3>
                    <form id="addCategoryForm" class="mt-20">
                        <div class="form-group">
                            <input type="text" id="catName" class="form-control" placeholder="Category Name" required>
                        </div>
                        <div class="form-group">
                            <input type="text" id="catDesc" class="form-control" placeholder="Description">
                        </div>
                        <button type="submit" class="btn btn-secondary">Add Category</button>
                    </form>
                    
                    <ul id="adminCategoryList" class="mt-20" style="list-style:none; padding:0;">
                        <!-- Categories injected here -->
                    </ul>
                </div>

                <div class="glass-card" style="grid-column: span 2;">
                    <h3>Questions</h3>
                    <form id="addQuestionForm" class="mt-20">
                        <div class="form-group flex" style="gap: 15px;">
                            <select id="qCategory" class="form-control" required>
                                <option value="">Select Category</option>
                            </select>
                            <input type="text" id="qText" class="form-control" placeholder="Question text" required style="flex:3;">
                        </div>
                        <div class="form-group flex" style="gap: 15px;">
                            <input type="text" id="qA" class="form-control" placeholder="Option A" required>
                            <input type="text" id="qB" class="form-control" placeholder="Option B" required>
                        </div>
                        <div class="form-group flex" style="gap: 15px;">
                            <input type="text" id="qC" class="form-control" placeholder="Option C" required>
                            <input type="text" id="qD" class="form-control" placeholder="Option D" required>
                        </div>
                        <div class="form-group">
                            <label>Correct Option</label>
                            <select id="qCorrect" class="form-control" required>
                                <option value="A">A</option>
                                <option value="B">B</option>
                                <option value="C">C</option>
                                <option value="D">D</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Add Question</button>
                    </form>
                    
                    <div class="table-wrapper mt-40">
                        <table id="adminQuestionsTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Text</th>
                                    <th>Category</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- Questions injected here -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    `
};
