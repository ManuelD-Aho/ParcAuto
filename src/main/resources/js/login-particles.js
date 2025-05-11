/**
 * Animation de particules pour l'écran de login
 * ParcAuto - MIAGE Holding (2025)
 */

// Configuration des particules
const particleConfig = {
    count: 50,           // Nombre de particules
    minSize: 2,          // Taille minimale
    maxSize: 6,          // Taille maximale
    minSpeed: 0.2,       // Vitesse minimale
    maxSpeed: 1.2,       // Vitesse maximale
    colors: [            // Couleurs possibles
        'rgba(255, 255, 255, 0.3)',
        'rgba(255, 255, 255, 0.2)',
        'rgba(255, 255, 255, 0.15)'
    ]
};

// Classe Particle pour gérer les particules individuelles
class Particle {
    constructor(container) {
        this.container = container;
        this.element = document.createElement('div');
        this.size = randomBetween(particleConfig.minSize, particleConfig.maxSize);
        this.color = randomFromArray(particleConfig.colors);
        this.x = Math.random() * 100;
        this.y = Math.random() * 100;
        this.speedX = randomBetween(particleConfig.minSpeed, particleConfig.maxSpeed) * (Math.random() > 0.5 ? 1 : -1);
        this.speedY = randomBetween(particleConfig.minSpeed, particleConfig.maxSpeed) * (Math.random() > 0.5 ? 1 : -1);

        // Appliquer les styles CSS
        this.element.style.position = 'absolute';
        this.element.style.width = `${this.size}px`;
        this.element.style.height = `${this.size}px`;
        this.element.style.backgroundColor = this.color;
        this.element.style.borderRadius = '50%';
        this.element.style.left = `${this.x}%`;
        this.element.style.top = `${this.y}%`;
        this.element.style.opacity = Math.random() * 0.6 + 0.4;
        this.element.style.boxShadow = `0 0 ${this.size * 2}px ${this.color}`;

        // Ajouter au conteneur
        this.container.appendChild(this.element);
    }

    // Mettre à jour la position de la particule
    update() {
        this.x += this.speedX;
        this.y += this.speedY;

        // Rebondir sur les bords
        if (this.x <= 0 || this.x >= 100) {
            this.speedX *= -1;
        }

        if (this.y <= 0 || this.y >= 100) {
            this.speedY *= -1;
        }

        // Mettre à jour la position dans le DOM
        this.element.style.left = `${this.x}%`;
        this.element.style.top = `${this.y}%`;
    }
}

// Fonctions utilitaires
function randomBetween(min, max) {
    return min + Math.random() * (max - min);
}

function randomFromArray(array) {
    return array[Math.floor(Math.random() * array.length)];
}

// Initialisation de l'animation
function initParticles() {
    const container = document.querySelector('.particles-container');
    if (!container) return;

    const particles = [];

    // Créer les particules
    for (let i = 0; i < particleConfig.count; i++) {
        particles.push(new Particle(container));
    }

    // Animer les particules
    function animate() {
        particles.forEach(particle => particle.update());
        requestAnimationFrame(animate);
    }

    animate();
}

// Initialiser quand le DOM est chargé
document.addEventListener('DOMContentLoaded', initParticles);

// Effet parallaxe sur le fond
function initParallax() {
    const container = document.querySelector('.background-container');
    if (!container) return;

    document.addEventListener('mousemove', (e) => {
        const x = e.clientX / window.innerWidth;
        const y = e.clientY / window.innerHeight;

        container.style.transform = `translate(${x * 15}px, ${y * 15}px)`;
    });
}

// Initialiser quand le DOM est chargé
document.addEventListener('DOMContentLoaded', initParallax);