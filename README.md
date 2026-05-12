# 🌾 HastaKala — Farmer's Craft Shop Manager

> An offline-first Android shop management app built for farmers and rural artisans which record sales, track inventory, and analyse earnings, all without needing the internet.

**Current Version: v1.5** &nbsp;|&nbsp; Kotlin + Jetpack Compose + Room DB + MVVM &nbsp;|&nbsp; Min SDK 26 (Android 8.0+)

---

## ▶ Try the Live Demo

> **No installation needed — runs directly in your browser.**

[![Live Demo — Try HastaKala Now](https://img.shields.io/badge/▶%20Live%20Demo-Try%20HastaKala%20Now-E07B39?style=for-the-badge&logo=android&logoColor=white)](https://appetize.io/app/b_gqquop42ru7kglqcfgkpc6rgri)

**[https://appetize.io/app/b_gqquop42ru7kglqcfgkpc6rgri](https://appetize.io/app/b_gqquop42ru7kglqcfgkpc6rgri)**

Click the link above to launch HastaKala on a virtual Android device in your browser. Tap around, record a sale, check the best-seller chart — the full app, live.

> *Hosted on [Appetize.io](https://appetize.io) · No sign-in required · Works on desktop and mobile browsers*

---

## 📖 About

**HastaKala** (हस्तकला — meaning *Handicraft* in Hindi) is a production-quality Android app designed for the Indian farming and rural artisan community. Farmers who make and sell handmade products have no easy way to run their business digitally. HastaKala brings them a clean, intuitive interface — no internet, no account, no complexity.

The app has gone through two versions of active development, growing from a solid foundation in v1.0 into a feature-rich, user-hardened tool in v1.5 — with real-world edge cases handled, data safety built in, and a polished UI throughout.

Made with ♥ by **Faisal Imam** — for our farmer and artisan community.

---

## 📦 Version History

### v1.5 — May 2026 &nbsp;*(Current)*
> **Theme: User Control, Data Safety & Interactivity**

This version focused on giving users full control over their data and making the app handle real-world selling scenarios gracefully.

**Quick Bill Enhancements**
- Added `+ Add Item` — users can create their own custom products with a name, emoji, and an optional default price
- Long-press any product card → Edit (name, emoji, default price) or Delete — works for all 18 pre-loaded products too
- Default price pre-fills automatically at billing time and remains editable per sale
- Color selection made optional — products like pickles, soaps, or ropes that have no color variants can be billed without picking a color
- Selected color name now appears with a colored dot for clarity (no more guessing by shade)
- Confirmation popup before saving any sale that exceeds ₹10,000
- Input validation blocks zero or negative values for quantity and price

**Income Enhancements**
- Every sale in the history list now has an Edit (✏️) and Delete (🗑️) button
- Editing a sale corrects quantity and price and recalculates the total in place
- Deletion requires confirmation to prevent accidental removal

**Best Sellers Enhancement**
- Pie chart made fully interactive — tap any slice to reveal a detail card showing product name, color, units sold, share percentage, and best-seller rank

**Polish**
- Custom app icon: warm orange-to-brown gradient with a white wheat stalk foreground
- About screen updated with v1.5 badge and revised usage guide

**Data Safety**
- All existing sales and stock records are preserved automatically when the app updates — no data is lost
- Products now store a default price and a flag indicating whether they were added by the user or came pre-loaded

---

### v1.0 — April 2026
> **Theme: Core Business Logic — Full Working App from Scratch**

The first release established the complete architecture and all five screens of the app, fully functional end-to-end.

**What was built**
- **Quick Bill screen** — a 3-column grid of all products; tap one, pick a color, enter quantity and price, see the running total, and save the sale with a single button
- **Best Sellers screen** — a visual pie chart of the top 8 selling product-color combinations, with a ranked breakdown list below
- **Stock screen** — full inventory list per product and color, a highlighted low-stock warning card for items running below 3 units, and an Add Stock dialog
- **Income screen** — toggle between This Week and This Month, a summary card showing total earnings and number of sales, and a full chronological sales history
- **Splash screen** — animated welcome screen with the app name, wheat icon, and farmer tagline
- **About screen** — scrollable guide covering how to use every feature, a full feature list, and creator credits
- **18 handcraft products** always available from the first launch — no setup needed
- **Color-wise tracking** across 10 colors (Red, Blue, Green, Yellow, Orange, Brown, Black, White, Pink, Purple)
- **All data stored on the device** — no internet, no cloud, works completely offline
- **Smooth navigation** with a persistent bottom menu bar and a separate About page

---

## ✨ Current Features (v1.5)

- 100% offline — no internet, no account, all data lives on the device
- Record a sale in seconds with optional color selection
- Add unlimited custom products with name, emoji, and default price
- Edit or delete any product (pre-loaded or custom)
- Edit or delete any recorded sale from the income history
- Confirmation popup for sales above ₹10,000
- Interactive best-seller pie chart with tap-to-detail
- Color-wise inventory tracking with low-stock alerts (< 3 units)
- Weekly and monthly income summaries
- Custom wheat-stalk app icon matching the warm brand palette

---

## 📱 Screens

| Screen | v1.0 | v1.5 additions |
|--------|------|----------------|
| 🧾 **Quick Bill** | Product grid, color picker, qty/price, save | Custom products, edit/delete, optional color, color names, ₹10k guard |
| 📊 **Best Seller** | Pie chart + breakdown list | Tap-to-detail interactive slices |
| 📦 **Stock** | Inventory list, low-stock alert, add stock | Updated |
| 💰 **Income** | Period toggle, summary card, history | Edit ✏️ and delete 🗑️ per sale |
| ℹ️ **About** | Static guide, v1.0 badge | Updated guide, v1.5 badge |

---

## 🛍️ Pre-loaded Products

18 handcraft products included by default — all editable, deletable, and extendable with your own custom items.

| | | |
|---|---|---|
| 🛍️ Banana Fiber Bag | 🔑 Keychain | 👜 Pouch |
| 🧺 Basket | ☕ Coaster | 🖼️ Wall Hanging |
| 🏺 Clay Pot | 🪢 Jute Rope | 🧵 Woven Mat |
| 🌸 Dried Flower Wreath | 🧼 Herbal Soap | 🕯️ Beeswax Candle |
| 🫙 Handmade Pickle | 🥄 Wooden Spoon | 🍵 Terracotta Bowl |
| 🧹 Natural Broom | 🧣 Handwoven Shawl | 🪔 Bamboo Lamp |

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| Architecture | MVVM — ViewModel + StateFlow + Repository |
| Database | Room DB v3 with KSP (compile-time processing) |
| Navigation | Jetpack Navigation Compose |
| Charts | MPAndroidChart |
| Build | AGP 9.2.1, Gradle 9.4.1 |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 36 |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest recommended)
- Android device or emulator running API 26+

### Clone & Run
```bash
git clone https://github.com/faisalimam1/HastaKalaApp.git
cd HastaKalaApp
```
Open in Android Studio → **Run** ▶

No API keys, no internet setup, no configuration needed. Just build and run.

---

## 👨‍💻 Developer

**Faisal Imam**
Built with dedication for the Indian farmer and artisan community.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
