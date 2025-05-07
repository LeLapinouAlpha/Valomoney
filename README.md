# 💸 Valomoney - v1.0.0 (NeoForge 21.1.159)

**Valomoney** adds a basic money system to Minecraft through new items, villager trades, and an interactive ATM block.

---

# Features
## Items

### 🪙 Monetary Items

> 📦Those items are not craftable

- **Coin** — 1$
- **Bill** — 5$

These items can be obtained via new Wandering Trader deals:
- 1 Gold Ingot → 1 Coin (limit: 64 trades)
- 1 Emerald → 1 Bill (limit: 32 trades)

---

### 💳 Bank Card

![image](https://github.com/user-attachments/assets/046c4699-568c-43a3-a35d-cda6c225223e)

The **Bank Card** is a special item that can be bound to a player:
- **Right-click** with it to bind it to your player.
- Once bound, it shows your current account balance in the tooltip.

> 🔒 The balance shown always reflects the *current player’s* money, not necessarily the owner of the card (known issue).

---

## Blocks

### 🏦 ATM Block

![image](https://github.com/user-attachments/assets/51d14950-2d43-4bbd-9d64-504f5d0c1877)

- Place and right-click the **ATM** to open a GUI.
- The ATM allows you to:
    - **Credit** money (coins or bills)
    - **Debit** money

> 🔒 The ATM block model is not yet finised (known issue)

---

# ⚠️ Known Issues

- The Bank Card's binding to a player is **not yet enforced**, meaning any player sees their own balance.
- The tooltip updates every frame by requesting the balance from the server, which may cause performance or network issues.
- The ATM block model is not yet finised

---

# 🔧 Requirements

- Minecraft **1.21.1**
- NeoForge **21.1.159**

---
