-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 04-06-2020 a las 08:28:56
-- Versión del servidor: 8.0.20
-- Versión de PHP: 7.2.24-0ubuntu0.18.04.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `stockcontrol`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `orders`
--

CREATE TABLE `orders` (
  `id_order` int NOT NULL,
  `id_provider` smallint NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `orders`
--

INSERT INTO `orders` (`id_order`, `id_provider`, `date`) VALUES
(7, 6, '2020-06-07'),
(9, 12, '2020-05-10'),
(10, 12, '2020-06-04'),
(11, 11, '2020-06-01'),
(12, 13, '2020-05-05'),
(13, 14, '2020-06-04');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `plates`
--

CREATE TABLE `plates` (
  `id_plate` mediumint NOT NULL,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `plates`
--

INSERT INTO `plates` (`id_plate`, `name`) VALUES
(1, 'Tostadas con tomate'),
(8, 'Sandwich Mixto'),
(9, 'Sandwich Vegetal'),
(10, 'Secreto Iberico'),
(11, 'Ensalada de filete de pollo'),
(12, 'Huevos rotos con gulas del norte'),
(13, 'Bocadillo de tortilla francesa');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `products`
--

CREATE TABLE `products` (
  `id_product` mediumint NOT NULL,
  `id_provider` smallint NOT NULL,
  `spanish_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `english_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `current_amount` mediumint NOT NULL,
  `spent_amount` mediumint NOT NULL,
  `minimum_amount` smallint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `products`
--

INSERT INTO `products` (`id_product`, `id_provider`, `spanish_name`, `english_name`, `current_amount`, `spent_amount`, `minimum_amount`) VALUES
(9, 6, 'Ambientador de Fresa', 'Strawberry Air Freshener', 3, 0, 1),
(10, 6, 'Friega suelo de Limón', 'Scrub lemon floor', 2, 0, 1),
(11, 6, 'Lavavajillas Finish Gold', 'dishwasher Finish Gold', 5, 0, 1),
(12, 7, 'Huevo', 'Egg', 114, 0, 90),
(13, 7, 'Pan de molde', 'Bread', 32, 0, 10),
(14, 9, 'Tomate', 'Tomato', 44, 0, 30),
(15, 9, 'Limón', 'Lemon', 28, 0, 20),
(16, 9, 'Lechuga', 'Lettuce', 54, 0, 10),
(17, 10, 'Filete de pollo', 'Chicken fillet', 38, 0, 20),
(18, 10, 'Secreto', 'Pork steak', 28, 0, 10),
(19, 10, 'Gulas del norte', 'Gluttony', 18, 0, 10),
(20, 13, 'Barril de Mahou', 'Mahou barrel', 2, 0, 1),
(21, 13, 'Barril de Mahou sin alcohol', 'Alcohol free mahou barrel', 6, 0, 1),
(22, 13, 'Tercio de Mahou', 'Bottled beer', 310, 0, 40),
(23, 11, 'Mayonesa', 'Mayonnaise', 11, 0, 5),
(24, 11, 'Pimientos rojos', 'Red Peppers', 30, 0, 20),
(25, 11, 'Bolsa de atún', 'Tuna bag', 14, 0, 3),
(26, 12, 'Baguette', 'Baguette', 33, 0, 12),
(27, 12, 'Napolitana de chocolate', 'Neapolitan chocolate', 231, 0, 8),
(28, 12, 'Croissant', 'Croissant', 395, 0, 30),
(29, 14, 'Lata de coca cola', 'coca cola can', 78, 0, 15),
(30, 14, 'Barril de coca cola', 'Coca cola barrel', 5, 0, 2),
(31, 14, 'Botella de coca cola', 'Coca cola bottle', 80, 0, 10),
(32, 10, 'Café', 'Coffee', 50, 0, 20);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `products_orders_relationship`
--

CREATE TABLE `products_orders_relationship` (
  `id_product` mediumint NOT NULL,
  `id_order` int NOT NULL,
  `ordered_amount` mediumint NOT NULL,
  `received_amount` mediumint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `products_orders_relationship`
--

INSERT INTO `products_orders_relationship` (`id_product`, `id_order`, `ordered_amount`, `received_amount`) VALUES
(9, 7, 10, 0),
(10, 7, 6, 0),
(20, 12, 5, 0),
(21, 12, 5, 3),
(22, 12, 200, 200),
(24, 11, 50, 0),
(27, 9, 100, 100),
(27, 10, 50, 0),
(28, 9, 200, 200),
(28, 10, 100, 0),
(30, 13, 10, 0),
(31, 13, 100, 50);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `products_plates_relationship`
--

CREATE TABLE `products_plates_relationship` (
  `id_product` mediumint NOT NULL,
  `id_plate` mediumint NOT NULL,
  `amount` tinyint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `products_plates_relationship`
--

INSERT INTO `products_plates_relationship` (`id_product`, `id_plate`, `amount`) VALUES
(12, 8, 1),
(12, 12, 1),
(12, 13, 1),
(13, 8, 2),
(13, 9, 2),
(14, 8, 1),
(14, 9, 1),
(14, 11, 1),
(15, 11, 1),
(16, 8, 1),
(16, 9, 1),
(16, 11, 1),
(17, 11, 1),
(18, 10, 1),
(19, 12, 1),
(23, 8, 1),
(23, 9, 1),
(26, 13, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `providers`
--

CREATE TABLE `providers` (
  `id_provider` smallint NOT NULL,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `phone_number` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `days_between_orders` smallint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `providers`
--

INSERT INTO `providers` (`id_provider`, `name`, `email`, `phone_number`, `days_between_orders`) VALUES
(6, 'Aldi', 'aldi@aldi.com', '623432312', 30),
(7, 'Día', 'supermercados_dia@gmail.es', '654321324', NULL),
(9, 'Fruterías De Hoy', 'hoy@fruterias.com', '623435654', 7),
(10, 'Makro', 'producto@makro.com', '654346587', NULL),
(11, 'Medina 3', 'medi@gmail.com', '658987542', NULL),
(12, 'Croissanterie', 'croissanterie@gmail.com', '686534231', 3),
(13, 'Roberto El Cervecero', 'robert@cervecero.com', '657341234', NULL),
(14, 'Coca Cola', 'proveedor@cocacola.es', '687234432', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `user_id` smallint NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `permissions` varchar(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `permissions`) VALUES
(1, 'administrador', 'administrador', '111111'),
(2, 'usuario', 'usuario', '100000'),
(3, 'jefe', 'jefe', '100010');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id_order`),
  ADD KEY `PROVIDER INDEX` (`id_provider`) USING BTREE;

--
-- Indices de la tabla `plates`
--
ALTER TABLE `plates`
  ADD PRIMARY KEY (`id_plate`);

--
-- Indices de la tabla `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id_product`),
  ADD KEY `PROVIDER INDEX` (`id_provider`);

--
-- Indices de la tabla `products_orders_relationship`
--
ALTER TABLE `products_orders_relationship`
  ADD PRIMARY KEY (`id_product`,`id_order`),
  ADD KEY `PRODUCT INDEX` (`id_product`),
  ADD KEY `ORDER INDEX` (`id_order`);

--
-- Indices de la tabla `products_plates_relationship`
--
ALTER TABLE `products_plates_relationship`
  ADD PRIMARY KEY (`id_product`,`id_plate`),
  ADD KEY `PRODUCT INDEX` (`id_product`),
  ADD KEY `PLATE INDEX` (`id_plate`);

--
-- Indices de la tabla `providers`
--
ALTER TABLE `providers`
  ADD PRIMARY KEY (`id_provider`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `orders`
--
ALTER TABLE `orders`
  MODIFY `id_order` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `plates`
--
ALTER TABLE `plates`
  MODIFY `id_plate` mediumint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `products`
--
ALTER TABLE `products`
  MODIFY `id_product` mediumint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT de la tabla `providers`
--
ALTER TABLE `providers`
  MODIFY `id_provider` smallint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `user_id` smallint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `ORDER PROVIDER RESTRICTION` FOREIGN KEY (`id_provider`) REFERENCES `providers` (`id_provider`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `PRODUCT PROVIDER RESTRICTION` FOREIGN KEY (`id_provider`) REFERENCES `providers` (`id_provider`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `products_orders_relationship`
--
ALTER TABLE `products_orders_relationship`
  ADD CONSTRAINT `PORS ORDER RESTRICTION` FOREIGN KEY (`id_order`) REFERENCES `orders` (`id_order`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PORS PRODUCT RESTRICTION` FOREIGN KEY (`id_product`) REFERENCES `products` (`id_product`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `products_plates_relationship`
--
ALTER TABLE `products_plates_relationship`
  ADD CONSTRAINT `PPRS PLATE RESTRICTION` FOREIGN KEY (`id_plate`) REFERENCES `plates` (`id_plate`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PPRS PRODUCT RESTRICTION` FOREIGN KEY (`id_product`) REFERENCES `products` (`id_product`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
