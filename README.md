<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Homeliness — Товары для дома</title>
    
    <style>
        :root {
            --main-color: #2d3436;
            --accent-color: #d35400;
            --bg-light: #f4f4f4;
            --text-white: #ffffff;
            --font-main: 'Segoe UI', Roboto, sans-serif;
            --transition: all 0.3s ease;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: var(--font-main);
            line-height: 1.6;
            color: var(--main-color);
            background-color: var(--bg-light);
        }

        .container {
            max-width: 1100px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .header {
            background: var(--text-white);
            padding: 1rem 0;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .nav {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            font-size: 1.5rem;
            font-weight: bold;
            color: var(--main-color);
            text-decoration: none;
        }

        .logo span { color: var(--accent-color); }

        .nav-list {
            display: flex;
            list-style: none;
        }

        .nav-list li { margin-left: 20px; }

        .nav-list a {
            text-decoration: none;
            color: var(--main-color);
            font-weight: 500;
            transition: var(--transition);
        }

        .nav-list a:hover { color: var(--accent-color); }

        .hero {
            height: 70vh;
            background: linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.6)), 
                        url('fonod.jpg') no-repeat center/cover;
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
            color: var(--text-white);
        }

        .hero-content h1 {
            font-size: 3.5rem;
            margin-bottom: 1rem;
            animation: fadeIn 1.5s ease-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .catalog { padding: 80px 0; }

        .section-title {
            text-align: center;
            margin-bottom: 40px;
            font-size: 2rem;
        }

        .grid-container {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 30px;
        }

        .product-card {
            background: rgb(245, 224, 224);
            border-radius: 12px;
            overflow: hidden;
            border: 1px solid #8c6464;
            transition: transform 0.3s;
        }

        .product-card:hover {
            transform: scale(1.03);
        }

        .product-card img {
            width: 100%;
            height: 250px;
            object-fit: cover;
        }

        .card-body { padding: 20px; }

        .price {
            display: block;
            margin-top: 10px;
            font-weight: bold;
            color: var(--accent-color);
            font-size: 1.2rem;
        }

        .contact-section {
            background: #ffffff;
            padding: 80px 0;
        }

        .contact-form {
            max-width: 600px;
            margin: 0 auto;
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .contact-form input, .contact-form textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #8c6464;
            border-radius: 6px;
            font-size: 1rem;
        }

        .contact-form input:focus {
            outline: none;
            border-color: var(--accent-color);
            box-shadow: 0 0 5px rgba(211, 84, 0, 0.3);
        }

        .btn-submit {
            background: var(--accent-color);
            color: white;
            border: none;
            padding: 15px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: bold;
            transition: var(--transition);
        }

        .btn-submit:hover { background: #e67e22; }

        @media (max-width: 992px) {
            .grid-container { grid-template-columns: repeat(2, 1fr); }
            .hero-content h1 { font-size: 2.5rem; }
        }

        @media (max-width: 600px) {
            .grid-container { grid-template-columns: 1fr; }
            .nav-list { display: none; }
            .hero-content h1 { font-size: 1.8rem; }
        }
    </style>
</head>
<body>

    <header class="header">
        <nav class="nav container">
            <a href="#" class="logo">Home<span>liness</span></a>
            <ul class="nav-list">
                <li><a href="#catalog">Каталог</a></li>
                <li><a href="#form">Заказать</a></li>
                <li><a href="#">О нас</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <section class="hero">
            <div class="hero-content container">
                <h1>Уют начинается с деталей</h1>
                <p>Откройте для себя коллекцию предметов декора для вашего дома</p>
            </div>
        </section>

        <section id="catalog" class="catalog container">
            <h2 class="section-title">Популярные товары</h2>
            <div class="grid-container">
                <article class="product-card">
                    <img src="vase.jpg" alt="Стильная ваза">
                    <div class="card-body">
                        <h3>Ваза</h3>
                        <p>Глиняная ваза ручной работы.</p>
                        <span class="price">6 899 ₽</span>
                    </div>
                </article>

                <article class="product-card">
                    <img src="pled.jpg" alt="Пледы">
                    <div class="card-body">
                        <h3>Мягкий плед</h3>
                        <p>Зелёный хлопчатый плед.</p>
                        <span class="price">3 499 ₽</span>
                    </div>
                </article>

                <article class="product-card">
                    <img src="lamp.jpg" alt="Лампа">
                    <div class="card-body">
                        <h3>Лампа настольная</h3>
                        <p>Металлическая лампа с холодным светом.</p>
                        <span class="price">2 999 ₽</span>
                    </div>
                </article>
            </div>
        </section>

        <section id="form" class="contact-section">
            <div class="container">
                <h2 class="section-title">Оформить заказ</h2>
                <form class="contact-form">
                    <input type="text" placeholder="Ваше имя" required>
                    <input type="email" placeholder="Ваш Email" required>
                    <textarea rows="5" placeholder="Какой товар вас интересует?"></textarea>
                    <button type="submit" class="btn-submit">Отправить заявку</button>
                </form>
            </div>
        </section>
    </main>

    <footer style="background: var(--main-color); color: rgb(255, 255, 255); padding: 40px 0; text-align: center;">
        <p>&copy; 2026 Homeliness — Магазин товаров для дома.</p>
    </footer>

</body>
</html>
