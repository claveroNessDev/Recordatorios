// Servidor simulado para la app Recordatorios (Actividad 3)
// Ejecutar: node server.js  →  http://localhost:3000
// Desde el emulador Android, el Mac se ve como 10.0.2.2  →  http://10.0.2.2:3000/
const express = require("express");
const fs = require("fs");
const path = require("path");

const PUERTO = 3000;
const ARCHIVO_DATOS = path.join(__dirname, "datos.json");

const app = express();
app.use(express.json());

// ---------- Datos: se guardan en datos.json para sobrevivir a los reinicios ----------
function leerDatos() {
  if (!fs.existsSync(ARCHIVO_DATOS)) return {};
  try {
    return JSON.parse(fs.readFileSync(ARCHIVO_DATOS, "utf8"));
  } catch {
    return {};
  }
}

function guardarDatos(datos) {
  fs.writeFileSync(ARCHIVO_DATOS, JSON.stringify(datos, null, 2));
}

let recordatorios = leerDatos(); // objeto { id: recordatorio }

// ---------- Simulación de fallas ----------
// normal   → responde bien
// error500 → todas las respuestas son "500 Error interno"
// lento    → responde después de 25 s (la app debe detectar un timeout)
let modo = "normal";

// Registra cada solicitud en la terminal y aplica el modo de simulación
app.use((req, res, next) => {
  console.log(`[${new Date().toLocaleTimeString()}] ${req.method} ${req.url}  (modo: ${modo})`);
  if (req.path.startsWith("/simulacion")) return next(); // el control nunca falla
  if (modo === "error500") return res.status(500).json({ error: "Error interno simulado" });
  if (modo === "lento") return setTimeout(next, 25000);
  next();
});

app.get("/simulacion", (req, res) => res.json({ modo }));

app.put("/simulacion/:nuevoModo", (req, res) => {
  const validos = ["normal", "error500", "lento"];
  if (!validos.includes(req.params.nuevoModo)) {
    return res.status(400).json({ error: `Modo inválido. Usa: ${validos.join(", ")}` });
  }
  modo = req.params.nuevoModo;
  console.log(`>>> Modo de simulación: ${modo}`);
  res.json({ modo });
});

// ---------- API de recordatorios ----------
app.get("/recordatorios", (req, res) => {
  res.json(Object.values(recordatorios));
});

app.get("/recordatorios/:id", (req, res) => {
  const recordatorio = recordatorios[req.params.id];
  if (!recordatorio) return res.status(404).json({ error: "No existe" });
  res.json(recordatorio);
});

// PUT = crear o reemplazar con un id conocido (idempotente: repetirlo NO duplica)
app.put("/recordatorios/:id", (req, res) => {
  const { id } = req.params;
  const cuerpo = req.body;
  const invalido =
    !cuerpo || cuerpo.id !== id || typeof cuerpo.titulo !== "string" || cuerpo.titulo.trim() === "";
  if (invalido) {
    return res.status(400).json({ error: "Solicitud inválida: el id no coincide o falta el título" });
  }
  const existia = id in recordatorios;
  recordatorios[id] = cuerpo;
  guardarDatos(recordatorios);
  res.status(existia ? 200 : 201).json(cuerpo); // 201 = creado, 200 = reemplazado
});

// DELETE idempotente: si ya no existe, igual responde 204
app.delete("/recordatorios/:id", (req, res) => {
  delete recordatorios[req.params.id];
  guardarDatos(recordatorios);
  res.status(204).end();
});

app.listen(PUERTO, () => {
  console.log(`Servidor simulado en http://localhost:${PUERTO}`);
  console.log(`Desde el emulador Android: http://10.0.2.2:${PUERTO}/`);
  console.log(`Recordatorios guardados: ${Object.keys(recordatorios).length}`);
});