const App = {
    state: {
        jwt: localStorage.getItem('jwt') || null,
        username: localStorage.getItem('username') || null,
        role: localStorage.getItem('role') || null,
        id: localStorage.getItem('userId') || null,
        currentQuizId: null,
        currentQuizLevel: null,
        quizQuestions: [],
        quizAnswers: {},
        currentQuestionIndex: 0,
        timerInterval: null
    },

    init() {
        this.defineRoutes();
        Router.init();
        this.updateNavbar();
    },

    showToast(message, type = 'success') {
        const toast = document.getElementById('toast');
        toast.textContent = message;
        toast.className = `toast ${type}`;
        setTimeout(() => {
            toast.classList.add('hidden');
        }, 3000);
    },

    updateNavbar() {
        const navLinks = document.getElementById('nav-links');
        if (!this.state.jwt) {
            navLinks.innerHTML = `
                <li><a href="#/login">Login</a></li>
                <li><a href="#/register">Register</a></li>
            `;
        } else {
            if (this.state.role === 'ROLE_ADMIN') {
                navLinks.innerHTML = `
                    <li><a href="#/admin">Admin Panel</a></li>
                    <li><a href="#" id="logoutBtn">Logout</a></li>
                `;
            } else {
                navLinks.innerHTML = `
                    <li><a href="#/dashboard">Dashboard</a></li>
                    <li><a href="#/leaderboard">Leaderboard</a></li>
                    <li><a href="#" id="logoutBtn">Logout</a></li>
                `;
            }
            document.getElementById('logoutBtn').addEventListener('click', (e) => {
                e.preventDefault();
                this.logout();
            });
        }
    },

    setSession(data) {
        this.state.jwt = data.token;
        this.state.username = data.username;
        this.state.role = data.role;
        this.state.id = data.id;
        localStorage.setItem('jwt', data.token);
        localStorage.setItem('username', data.username);
        localStorage.setItem('role', data.role);
        localStorage.setItem('userId', data.id);
    },

    logout() {
        this.state = { jwt: null, username: null, role: null, id: null };
        localStorage.clear();
        this.updateNavbar();
        Router.navigate('/login');
    },

    defineRoutes() {
        Router.addRoute('/login', views.login, () => this.initLogin());
        Router.addRoute('/register', views.register, () => this.initRegister());
        Router.addRoute('/dashboard', views.studentDashboard, () => this.initDashboard());
        Router.addRoute('/level', views.levelSelect, (id) => this.initLevelSelect(id));
        Router.addRoute('/quiz', views.quiz, (params) => this.initQuiz(params));
        Router.addRoute('/results', views.results, () => this.initResults());
        Router.addRoute('/leaderboard', views.leaderboard, () => this.initLeaderboard());
        Router.addRoute('/admin', views.adminDashboard, () => this.initAdmin());
    },

    // --- Page Initializers ---

    initLogin() {
        document.getElementById('loginForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const u = document.getElementById('username').value;
            const p = document.getElementById('password').value;
            try {
                const res = await api.login(u, p);
                this.setSession(res);
                this.showToast('Login successful!');
                if(this.state.role === 'ROLE_ADMIN') {
                    Router.navigate('/admin');
                } else {
                    Router.navigate('/dashboard');
                }
            } catch (err) {
                this.showToast('Login failed: ' + err.message, 'error');
            }
        });
    },

    initRegister() {
        document.getElementById('registerForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const u = document.getElementById('regUsername').value;
            const p = document.getElementById('regPassword').value;
            const r = document.getElementById('regRole').value;
            try {
                await api.register(u, p, r);
                this.showToast('Registration successful! Please login.');
                Router.navigate('/login');
            } catch (err) {
                this.showToast('Reg failed: ' + err.message, 'error');
            }
        });
    },

    async initDashboard() {
        try {
            const [categories, attempts] = await Promise.all([
                api.getCategories(),
                api.getAttempts()
            ]);

            const categoryGrid = document.getElementById('categoryGrid');
            if (categories.length === 0) {
                categoryGrid.innerHTML = '<p style="grid-column: 1 / -1; padding: 20px; font-style: italic; color: #8b949e;">No categories available yet. An Admin needs to create some first!</p>';
            } else {
                categoryGrid.innerHTML = categories.map(cat => `
                    <div class="category-card" onclick="Router.navigate('/level/${cat.id}')">
                        <h3>${cat.name}</h3>
                        <p>${cat.description || ''}</p>
                        <div class="mt-20">Take Quiz <i class="fa-solid fa-arrow-right"></i></div>
                    </div>
                `).join('');
            }

            const tbody = document.querySelector('#attemptsTable tbody');
            if (attempts.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; font-style: italic; color: #8b949e;">No attempts yet. Go take a quiz!</td></tr>';
            } else {
                tbody.innerHTML = attempts.map(att => `
                    <tr>
                        <td>${att.categoryName}</td>
                        <td>${att.score}</td>
                        <td>${att.totalQuestions}</td>
                        <td>${new Date(att.submitTime).toLocaleString()}</td>
                    </tr>
                `).join('');
            }

        } catch (err) {
            this.showToast('Error loading dashboard', 'error');
        }
    },

    initLevelSelect(categoryId) {
        if(!categoryId) { Router.navigate('/dashboard'); return; }
        document.getElementById('btnBasics').onclick = () => Router.navigate(`/quiz/${categoryId}/BASICS`);
        document.getElementById('btnMedium').onclick = () => Router.navigate(`/quiz/${categoryId}/MEDIUM`);
        document.getElementById('btnAdvanced').onclick = () => Router.navigate(`/quiz/${categoryId}/ADVANCED`);
    },

    async initQuiz(params) {
        if(!params || !params.categoryId || !params.level) { Router.navigate('/dashboard'); return; }
        this.state.currentQuizId = params.categoryId;
        this.state.currentQuizLevel = params.level;
        this.state.quizAnswers = {};
        this.state.currentQuestionIndex = 0;

        try {
            const questions = await api.getQuiz(params.categoryId, params.level);
            if(questions.length === 0) {
                this.showToast('No questions available in this category.', 'error');
                Router.navigate('/dashboard');
                return;
            }
            this.state.quizQuestions = questions;
            document.getElementById('quizCategoryName').textContent = 'Quiz Time!';
            
            this.startTimer(5 * 60); // 5 minutes timer
            this.renderQuestion();

            // Next Question Button
            document.getElementById('nextBtn').addEventListener('click', () => {
                this.saveCurrentAnswer();
                this.state.currentQuestionIndex++;
                this.renderQuestion();
            });

            // Submit Quiz Button
            document.getElementById('submitQuizBtn').addEventListener('click', () => {
                this.saveCurrentAnswer();
                this.submitQuiz();
            });

        } catch (err) {
            this.showToast('Failed to load quiz', 'error');
        }
    },

    renderQuestion() {
        const qIndex = this.state.currentQuestionIndex;
        const q = this.state.quizQuestions[qIndex];
        
        document.getElementById('questionText').textContent = `${qIndex + 1}. ${q.text}`;
        
        // Form Options
        const optsHtml = [
            {val: 'A', text: q.optionA},
            {val: 'B', text: q.optionB},
            {val: 'C', text: q.optionC},
            {val: 'D', text: q.optionD}
        ].map(opt => {
            const isAnswered = !!this.state.quizAnswers[q.id];
            const isSelected = this.state.quizAnswers[q.id] === opt.val;
            
            let extraClass = '';
            if (isAnswered) {
                if (opt.val === q.correctOption) extraClass = 'correct-ans';
                else if (isSelected) extraClass = 'wrong-ans';
            }
            
            return `
            <input type="radio" name="answer" id="opt${opt.val}" value="${opt.val}" class="option-input" 
                ${isSelected ? 'checked' : ''} ${isAnswered ? 'disabled' : ''}>
            <label for="opt${opt.val}" class="option-label ${extraClass}" id="label${opt.val}">${opt.val}. ${opt.text}</label>
            `;
        }).join('');
        
        document.getElementById('optionsContainer').innerHTML = optsHtml;
        document.getElementById('questionProgress').textContent = `Question ${qIndex + 1} of ${this.state.quizQuestions.length}`;
        
        const submitAnswerBtn = document.getElementById('submitAnswerBtn');
        const nextBtn = document.getElementById('nextBtn');
        const submitBtn = document.getElementById('submitQuizBtn');
        
        // Reset button displays for new question
        nextBtn.style.display = 'none';
        submitBtn.style.display = 'none';
        
        // If already answered (e.g., navigating back, though not supported yet, safe check)
        if (this.state.quizAnswers[q.id]) {
            submitAnswerBtn.style.display = 'none';
            if (qIndex < this.state.quizQuestions.length - 1) {
                nextBtn.style.display = 'inline-block';
            } else {
                submitBtn.style.display = 'inline-block';
            }
        } else {
            submitAnswerBtn.style.display = 'none'; // Hidden until option selected
            submitAnswerBtn.onclick = (e) => {
                e.preventDefault();
                const selectedInput = document.querySelector('.option-input:checked');
                if (!selectedInput) return;
                
                const selectedVal = selectedInput.value;
                this.state.quizAnswers[q.id] = selectedVal;
                
                document.querySelectorAll('.option-input').forEach(inp => inp.disabled = true);
                
                const correctLabel = document.getElementById('label' + q.correctOption);
                if (correctLabel) correctLabel.classList.add('correct-ans');
                
                if (selectedVal !== q.correctOption) {
                    const selectedLabel = document.getElementById('label' + selectedVal);
                    if (selectedLabel) selectedLabel.classList.add('wrong-ans');
                }
                
                submitAnswerBtn.style.display = 'none';
                if (qIndex < this.state.quizQuestions.length - 1) {
                    nextBtn.style.display = 'inline-block';
                } else {
                    submitBtn.style.display = 'inline-block';
                }
            };
        }

        // Setup listeners for option selection logic (highlighting)
        document.querySelectorAll('.option-input').forEach(input => {
            input.addEventListener('change', (e) => {
                if(this.state.quizAnswers[q.id]) return; // already submitted
                submitAnswerBtn.style.display = 'inline-block';
            });
        });
    },

    saveCurrentAnswer() {
        // Now handled instantly on change, so nothing to do here
    },

    startTimer(seconds) {
        clearInterval(this.state.timerInterval);
        let timeRemaining = seconds;
        const timerEl = document.getElementById('quizTimer');
        
        this.state.timerInterval = setInterval(() => {
            if(timeRemaining <= 0) {
                clearInterval(this.state.timerInterval);
                this.showToast("Time's up! Auto-submitting...");
                this.saveCurrentAnswer();
                this.submitQuiz();
                return;
            }
            timeRemaining--;
            const m = Math.floor(timeRemaining / 60).toString().padStart(2, '0');
            const s = (timeRemaining % 60).toString().padStart(2, '0');
            timerEl.textContent = `${m}:${s}`;
            
            if(timeRemaining < 60) timerEl.style.color = 'var(--danger-color)';
        }, 1000);
    },

    async submitQuiz() {
        clearInterval(this.state.timerInterval);
        try {
            const result = await api.submitQuiz(this.state.currentQuizId, this.state.currentQuizLevel, this.state.quizAnswers);
            sessionStorage.setItem('lastResult', JSON.stringify(result));
            sessionStorage.setItem('lastQuestions', JSON.stringify(this.state.quizQuestions));
            sessionStorage.setItem('lastAnswers', JSON.stringify(this.state.quizAnswers));
            Router.navigate('/results');
        } catch (err) {
            this.showToast('Failed to submit quiz', 'error');
        }
    },

    initResults() {
        const resultRaw = sessionStorage.getItem('lastResult');
        const questionsRaw = sessionStorage.getItem('lastQuestions');
        const answersRaw = sessionStorage.getItem('lastAnswers');
        
        if(!resultRaw || !questionsRaw || !answersRaw) {
            Router.navigate('/dashboard');
            return;
        }
        
        const result = JSON.parse(resultRaw);
        const questions = JSON.parse(questionsRaw);
        const answers = JSON.parse(answersRaw);
        
        document.getElementById('resultScore').textContent = result.score;
        document.getElementById('resultTotal').textContent = result.totalQuestions;
        
        // Render Review
        const reviewHtml = questions.map((q, index) => {
            const userAnswer = answers[q.id];
            // fallback to q.correctOption if correctAnswers from backend missing or empty string
            const correctAnswer = result.correctAnswers && result.correctAnswers[q.id] ? result.correctAnswers[q.id] : q.correctOption;
            
            return `
                <div class="glass-card mt-20" style="padding: 20px;">
                    <h4 style="margin-bottom: 15px;">${index + 1}. ${q.text}</h4>
                    <div style="display: flex; flex-direction: column; gap: 8px;">
                        <div class="option-label ${userAnswer === 'A' ? (userAnswer === correctAnswer ? 'correct-ans' : 'wrong-ans') : (correctAnswer === 'A' ? 'correct-ans' : '')}" style="margin-bottom: 0; cursor: default;">
                            A. ${q.optionA} ${correctAnswer === 'A' ? '<i class="fa-solid fa-check" style="float:right; color:var(--secondary-color);"></i>' : ''} ${userAnswer === 'A' && userAnswer !== correctAnswer ? '<i class="fa-solid fa-times" style="float:right; color:var(--danger-color);"></i>' : ''}
                        </div>
                        <div class="option-label ${userAnswer === 'B' ? (userAnswer === correctAnswer ? 'correct-ans' : 'wrong-ans') : (correctAnswer === 'B' ? 'correct-ans' : '')}" style="margin-bottom: 0; cursor: default;">
                            B. ${q.optionB} ${correctAnswer === 'B' ? '<i class="fa-solid fa-check" style="float:right; color:var(--secondary-color);"></i>' : ''} ${userAnswer === 'B' && userAnswer !== correctAnswer ? '<i class="fa-solid fa-times" style="float:right; color:var(--danger-color);"></i>' : ''}
                        </div>
                        <div class="option-label ${userAnswer === 'C' ? (userAnswer === correctAnswer ? 'correct-ans' : 'wrong-ans') : (correctAnswer === 'C' ? 'correct-ans' : '')}" style="margin-bottom: 0; cursor: default;">
                            C. ${q.optionC} ${correctAnswer === 'C' ? '<i class="fa-solid fa-check" style="float:right; color:var(--secondary-color);"></i>' : ''} ${userAnswer === 'C' && userAnswer !== correctAnswer ? '<i class="fa-solid fa-times" style="float:right; color:var(--danger-color);"></i>' : ''}
                        </div>
                        <div class="option-label ${userAnswer === 'D' ? (userAnswer === correctAnswer ? 'correct-ans' : 'wrong-ans') : (correctAnswer === 'D' ? 'correct-ans' : '')}" style="margin-bottom: 0; cursor: default;">
                            D. ${q.optionD} ${correctAnswer === 'D' ? '<i class="fa-solid fa-check" style="float:right; color:var(--secondary-color);"></i>' : ''} ${userAnswer === 'D' && userAnswer !== correctAnswer ? '<i class="fa-solid fa-times" style="float:right; color:var(--danger-color);"></i>' : ''}
                        </div>
                    </div>
                </div>
            `;
        }).join('');

        const reviewContainer = document.getElementById('reviewContainer');
        if(reviewContainer) {
            reviewContainer.innerHTML = `<h3 class="highlight" style="margin-bottom:20px;">Quiz Review</h3>${reviewHtml}`;
        }
        
        sessionStorage.removeItem('lastResult');
        sessionStorage.removeItem('lastQuestions');
        sessionStorage.removeItem('lastAnswers');
    },

    async initLeaderboard() {
        try {
            const data = await api.getLeaderboard();
            const tbody = document.querySelector('#leaderboardTable tbody');
            tbody.innerHTML = data.map((entry, index) => `
                <tr>
                    <td>${index + 1}</td>
                    <td>${entry.username}</td>
                    <td>${entry.categoryName}</td>
                    <td>${entry.score} / ${entry.totalQuestions}</td>
                    <td>${new Date(entry.submitTime).toLocaleDateString()}</td>
                </tr>
            `).join('');
        } catch (err) {
            this.showToast('Failed to load leaderboard', 'error');
        }
    },

    async initAdmin() {
        try {
            await this.loadAdminCategories();
            await this.loadAdminQuestions();

            // Setup Adds
            document.getElementById('addCategoryForm').addEventListener('submit', async (e) => {
                e.preventDefault();
                await api.adminCreateCategory(document.getElementById('catName').value, document.getElementById('catDesc').value);
                this.showToast('Category created!');
                this.loadAdminCategories();
            });

            document.getElementById('addQuestionForm').addEventListener('submit', async (e) => {
                e.preventDefault();
                await api.adminCreateQuestion(
                    document.getElementById('qCategory').value,
                    document.getElementById('qText').value,
                    document.getElementById('qA').value,
                    document.getElementById('qB').value,
                    document.getElementById('qC').value,
                    document.getElementById('qD').value,
                    document.getElementById('qCorrect').value
                );
                this.showToast('Question added!');
                this.loadAdminQuestions();
                e.target.reset();
            });

        } catch (err) {
            this.showToast('Failed to load admin dashboard', 'error');
        }
    },

    async loadAdminCategories() {
        const categories = await api.adminGetCategories();
        
        // Load into list
        const list = document.getElementById('adminCategoryList');
        list.innerHTML = categories.map(c => `
            <li class="flex justify-between" style="padding: 10px; border-bottom: 1px solid var(--glass-border)">
                <span>${c.name}</span>
                <button onclick="App.deleteCategory(${c.id})" class="btn btn-danger" style="padding: 5px 10px; font-size: 0.8rem;">Delete</button>
            </li>
        `).join('');

        // Load into select dropdown for questions
        const select = document.getElementById('qCategory');
        select.innerHTML = '<option value="">Select Category</option>' + categories.map(c => `
            <option value="${c.id}">${c.name}</option>
        `).join('');
    },

    async loadAdminQuestions() {
        const questions = await api.adminGetQuestions();
        const tbody = document.querySelector('#adminQuestionsTable tbody');
        tbody.innerHTML = questions.map(q => `
            <tr>
                <td>${q.id}</td>
                <td>${q.text.substring(0, 30)}...</td>
                <td>${q.category.name}</td>
                <td>
                    <button onclick="App.deleteQuestion(${q.id})" class="btn btn-danger" style="padding: 5px 10px; font-size: 0.8rem;">Delete</button>
                </td>
            </tr>
        `).join('');
    },

    async deleteCategory(id) {
        if(confirm('Delete this category?')) {
            await api.adminDeleteCategory(id);
            this.loadAdminCategories();
        }
    },
    async deleteQuestion(id) {
        if(confirm('Delete this question?')) {
            await api.adminDeleteQuestion(id);
            this.loadAdminQuestions();
        }
    }
};

window.onload = () => App.init();
