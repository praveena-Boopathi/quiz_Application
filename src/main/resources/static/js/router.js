const Router = {
    routes: {},
    init() {
        window.addEventListener('hashchange', () => this.handleRoute());
        this.handleRoute(); // initial load
    },
    
    addRoute(path, template, controller) {
        this.routes[path] = { template, controller };
    },
    
    navigate(path) {
        window.location.hash = path;
    },
    
    handleRoute() {
        let path = window.location.hash.slice(1) || '/';
        let idParam = null;

        // dynamic routing for level/:categoryId
        if(path.startsWith('/level/')) {
            const parts = path.split('/');
            idParam = parts[2];
            path = '/level';
        }

        // dynamic routing for quiz/:categoryId/:level
        if(path.startsWith('/quiz/')) {
            const parts = path.split('/');
            idParam = { categoryId: parts[2], level: parts[3] };
            path = '/quiz';
        }

        const route = this.routes[path];
        
        const appRoot = document.getElementById('app-root');
        
        if (route) {
            appRoot.innerHTML = route.template;
            
            // Re-bind navbar based on auth
            App.updateNavbar();
            
            if (route.controller) {
                route.controller(idParam);
            }
        } else {
            // Not found or default
            if(App.state.jwt) {
                if(App.state.role === 'ROLE_ADMIN') {
                    this.navigate('/admin');
                } else {
                    this.navigate('/dashboard');
                }
            } else {
                this.navigate('/login');
            }
        }
    }
};
