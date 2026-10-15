# Byow — Coinlight Caverns

![Pixel-art design board](assets/byow-pixel-design.png)

**Byow** is a solo pixel-game project: generate a deterministic dungeon, wander with WASD, switch a
wall-blocked line of sight with `B`, and collect every gold coin.

> **Solo project:** designed and implemented independently by me.

## Highlights

- Primary ambition feature: toggleable line of sight with wall occlusion.
- Secondary ambition feature: coin collection, HUD progress, and return-to-menu completion flow.
- Deterministic seeded world generation with save/replay support.
- Pixel-art direction documented in [`docs/design.md`](docs/design.md).

## Controls

`N` new game · `L` load · `Q` quit/save · `WASD` move · `B` toggle sight · `C` enable coin mode.

## Run

Open the `proj3` folder in IntelliJ IDEA with the `algs4.jar` dependency configured, then run
`core.Main`.

## Project structure

```text
assets/                         pixel-art concept board
docs/design.md                  product and technical design
proj3/src/core/                 menus, world generation, gameplay
proj3/src/tileengine/           tiles, renderer, line-of-sight utility
```

This is a solo project completed by me and is intended for educational use.
