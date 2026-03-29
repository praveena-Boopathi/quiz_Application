const API_URL = '/api';

const api = {
    getToken() {
        return localStorage.getItem('jwt');
    },

    async request(endpoint, method = 'GET', body = null) {
        const headers = {
            'Content-Type': 'application/json'
        };

        const token = this.getToken();
        // Do not attach token for authentication endpoints
        if (token && !endpoint.startsWith('/auth')) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const options = {
            method,
            headers
        };

        if (body) {
            options.body = JSON.stringify(body);
        }

        try {
            const response = await fetch(`${API_URL}${endpoint}`, options);
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'API Request Failed');
            }
            // Some endpoints return just text (like OK)
            const contentType = response.headers.get("content-type");
            if (contentType && contentType.indexOf("application/json") !== -1) {
                return await response.json();
            } else {
                return await response.text();
            }
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },

    // Auth
    login(username, password) {
        return this.request('/auth/login', 'POST', { username, password });
    },
    register(username, password, role) {
        return this.request('/auth/signup', 'POST', { username, password, role });
    },

    // Student
    getCategories() {
        return this.request('/student/categories');
    },
    getQuiz(categoryId, level) {
        return this.request(`/student/quiz/${categoryId}/${level}`);
    },
    submitQuiz(categoryId, level, answers) {
        return this.request('/student/quiz/submit', 'POST', { categoryId, level, answers });
    },
    getAttempts() {
        return this.request('/student/dashboard/attempts');
    },
    getLeaderboard() {
        return this.request('/student/leaderboard');
    },

    // Admin
    adminGetCategories() {
        return this.request('/admin/categories');
    },
    adminCreateCategory(name, description) {
        return this.request('/admin/categories', 'POST', { name, description });
    },
    adminDeleteCategory(id) {
        return this.request(`/admin/categories/${id}`, 'DELETE');
    },
    adminGetQuestions() {
        return this.request('/admin/questions');
    },
    adminCreateQuestion(categoryId, text, optionA, optionB, optionC, optionD, correctOption) {
        return this.request('/admin/questions', 'POST', { categoryId, text, optionA, optionB, optionC, optionD, correctOption });
    },
    adminDeleteQuestion(id) {
        return this.request(`/admin/questions/${id}`, 'DELETE');
    }
};
