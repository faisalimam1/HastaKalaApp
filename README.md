# 🌾 HastaKala — Farmer's Craft Shop Manager

> A simple, offline-first Android app built for farmers and rural artisans to manage their handmade craft business — track sales, monitor stock, and view earnings, all without needing the internet.

---

## 📖 About

**HastaKala** (हस्तकला — meaning *Handicraft* in Hindi) is a lightweight shop management app designed specifically for the Indian farming and rural artisan community. Farmers who make and sell handmade products often have no easy way to track their business. HastaKala solves this with a clean, simple interface that anyone can use.

Made with ♥ by **Faisal Imam** — for our farmer community.

---

## 🆕 What's New in v1.5

### Quick Bill
- **Custom products** — tap `+ Add Item` to add your own products with a name, emoji, and optional default price
- **Edit / Delete products** — long-press any product card to edit its name, emoji, or default price, or remove it entirely (works for both pre-loaded and custom products)
- **Default price pre-fill** — if a product has a saved default price, it auto-fills when you go to bill it; you can still change it per sale
- **Color is now optional** — tap **No Color →** to skip color selection for products like pickles, soaps, etc. that don't have color variants
- **Color name on selection** — when you pick a color, its name appears beside a colored dot so there's no guessing
- **Large sale confirmation** — a popup asks "Are you sure?" before saving any sale that exceeds ₹10,000
- **Input validation** — quantity and price fields block zero or negative values with a clear error message

### Income
- **Edit sales** — tap ✏️ on any sale to correct the quantity or price (total recalculates automatically)
- **Delete sales** — tap 🗑️ to remove a sale recorded by mistake, with a confirmation before deletion

### Best Sellers
- **Interactive pie chart** — tap any slice to see a detail card with the product name, color, units sold, percentage of total sales, and its rank

### App Icon
- Brand-new custom icon: warm orange-to-brown gradient background with a white wheat stalk — replaces the default Android robot

### About
- Version badge updated to 1.5
- How to Use and Features sections reflect all new v1.5 capabilities

### Under the Hood
- Room DB migrated from v2 → v3 (adds `defaultPrice` and `isCustom` columns to products — **no data loss on upgrade**)
- New DAO methods: `updateSale`, `deleteSale`, `updateProduct`, `deleteProduct`, `deleteStockByProduct`

---

## ✨ Features

- **100% Offline** — works without internet, all data stored on device
- **Quick Bill** — record a sale in seconds; color selection is optional
- **Custom products** — add your own items beyond the 18 pre-loaded ones
- **Default prices** — set a default price per product; edit freely at billing time
- **Edit / Delete sales** — correct any mistake directly from Income history
- **Best Seller Analytics** — interactive pie chart with tap-to-detail slices
- **Stock Tracking** — monitor inventory with low-stock alerts (< 3 units)
- **Income Summary** — weekly and monthly earnings with full sales history
- **18 handcraft products** pre-loaded, all editable and deletable
- **Color-wise tracking** — track stock and sales by color where applicable
- **Large sale guard** — confirmation popup for sales above ₹10,000
- **Custom app icon** — warm wheat stalk icon matching the app's identity
- **Splash screen** with a warm welcome for the farmer community

---

## 📱 Screens

| Screen | Description |
|--------|-------------|
| 🧾 **Quick Bill** | Select product → pick color (optional) → enter qty & price → save sale |
| 📊 **Best Seller** | Interactive pie chart; tap a slice for full product details |
| 📦 **Stock** | Inventory per product/color, add stock, low-stock warnings |
| 💰 **Income** | Total earnings for the week or month + editable sale history |
| ℹ️ **About** | App info, how-to guide, version, and creator |

---

## 🛍️ Pre-loaded Products

18 farmer-made handcraft products included by default. All can be edited, deleted, or supplemented with your own custom items.

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
| Database | Room DB v3 (with KSP) |
| Navigation | Jetpack Navigation Compose |
| Architecture | MVVM (ViewModel + StateFlow) |
| Charts | MPAndroidChart |
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

## 📦 Version History

| Version | Date | Notes |
|---------|------|-------|
| **v1.5** | May 2026 | Custom products, editable sales, interactive chart, optional color, new app icon |
| **v1.0** | May 2026 | Initial release — Quick Bill, Best Seller, Stock, Income, Splash, About, 18 products |

---

## 👨‍💻 Developer

**Faisal Imam**
Built with dedication for the Indian farmer and artisan community.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
