/**
 * This  package provides classes
 * and interfaces for managing the economy of a Minecraft server. This includes
 * functionality related to player wallets, economic transactions, and data
 * persistence.
 * <p>
 * The main components of this package are:
 * <ul>
 *     <li>{@link fr.valorantage.valomoney.backend.economy.Wallet}: Represents
 *     a virtual wallet for a player, storing their balance and providing methods
 *     for adding and withdrawing money, saving and restoring them in binary files.</li>
 *     <li>{@link fr.valorantage.valomoney.backend.economy.EconomyManager}: Serves
 *     as an interface for managing player wallets, facilitating transactions,
 *     and interacting with the economy system.</li>
 *     <li>{@link fr.valorantage.valomoney.backend.economy.EconomyFileManager}:
 *     Handles the saving and loading of player wallets to and from files.</li>
 * </ul>
 * <p>
 * This package is designed to provide a flexible and extensible framework for
 * implementing and managing an in-game economy system within a Minecraft server.
 * It allows for the creation of virtual wallets for players, supports economic
 * transactions between players, and provides mechanisms for persisting wallet
 * data for persistence across server restarts.
 */
package fr.valorantage.valomoney.backend.economy;