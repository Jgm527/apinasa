// Sample data for the landing's examples. Shaped like the real responses of each API, but
// written by hand: the landing never calls NASA, so DEMO_KEY's quota is not spent on visitors.

/** NeoWs feed for one week: each asteroid's closest approach and estimated size. */
export const asteroids = [
  { name: '2026 SK3', distanceLd: 0.42, diameterM: 9, hazardous: false },
  { name: '2026 TA', distanceLd: 0.87, diameterM: 14, hazardous: false },
  { name: '2026 RX9', distanceLd: 1.9, diameterM: 31, hazardous: false },
  { name: '2026 SF1', distanceLd: 3.4, diameterM: 22, hazardous: false },
  { name: '2019 UO7', distanceLd: 5.1, diameterM: 68, hazardous: false },
  { name: '2026 QW4', distanceLd: 7.8, diameterM: 120, hazardous: false },
  { name: '2016 CO246', distanceLd: 11.6, diameterM: 45, hazardous: false },
  { name: '2005 YY128', distanceLd: 13.2, diameterM: 410, hazardous: true },
  { name: '2021 PJ1', distanceLd: 18.5, diameterM: 190, hazardous: false },
  { name: '2001 SN263', distanceLd: 24.3, diameterM: 1800, hazardous: true },
  { name: '2014 JN', distanceLd: 31.7, diameterM: 260, hazardous: false },
  { name: '1999 RM45', distanceLd: 46.9, diameterM: 950, hazardous: true },
]

/** Asteroids found per day of that week, from the length of each date's list. */
export const asteroidsPerDay = [11, 14, 9, 12, 16, 13, 10]
export const weekDays = ['28 sep', '29 sep', '30 sep', '1 oct', '2 oct', '3 oct', '4 oct']

/** DONKI FLR: solar flares, newest first. */
export const flares = [
  { date: '3 oct', peak: '14:12', classType: 'X1.3', region: 14232, instrument: 'GOES-19: EXIS 1.0-8.0' },
  { date: '1 oct', peak: '06:47', classType: 'M2.4', region: 14232, instrument: 'GOES-19: EXIS 1.0-8.0' },
  { date: '28 sep', peak: '22:05', classType: 'M1.1', region: 14229, instrument: 'GOES-19: EXIS 1.0-8.0' },
  { date: '25 sep', peak: '09:31', classType: 'C8.7', region: 14225, instrument: 'GOES-19: EXIS 1.0-8.0' },
  { date: '21 sep', peak: '17:58', classType: 'M5.0', region: 14221, instrument: 'GOES-19: EXIS 1.0-8.0' },
]

/**
 * EPIC natural: one day's pictures, each with the time it was taken and the point of the Earth at
 * the centre of the picture (`centroid_coordinates.lon`). The Earth turns 15° an hour under the
 * camera, so the longitude falls steadily through the day.
 */
export const epicDay = [
  { time: 0.6, lon: 173.8 },
  { time: 2.4, lon: 146.9, label: 'Japón' },
  { time: 4.2, lon: 119.9 },
  { time: 6.0, lon: 92.9 },
  { time: 7.8, lon: 65.9, label: 'India' },
  { time: 9.6, lon: 38.9 },
  { time: 11.4, lon: 11.9, label: 'África' },
  { time: 13.2, lon: -15.1 },
  { time: 15.0, lon: -42.1, label: 'Brasil' },
  { time: 16.8, lon: -69.1 },
  { time: 18.6, lon: -96.1, label: 'México' },
  { time: 20.4, lon: -123.1 },
  { time: 22.2, lon: -150.1 },
]

/** TechTransfer patents: from each result, its case number, title and category. */
export const patents = [
  { id: 'LEW-TOPS-155', title: 'Aleación de níquel para turbinas de alta temperatura', category: 'Materiales' },
  { id: 'ARC-TOPS-92', title: 'Sensor de gases de nanotubos de carbono para el móvil', category: 'Sensores' },
  { id: 'LAR-TOPS-301', title: 'Ala con superficie que se deforma para reducir el ruido', category: 'Aeronáutica' },
  { id: 'GSC-TOPS-207', title: 'Algoritmo de compresión de imágenes sin pérdida', category: 'Software' },
  { id: 'MSC-TOPS-88', title: 'Filtro de agua por ósmosis inversa para misiones largas', category: 'Salud' },
  { id: 'KSC-TOPS-64', title: 'Recubrimiento anticorrosión para estructuras metálicas', category: 'Materiales' },
  { id: 'JPL-TOPS-118', title: 'Cámara de bajo consumo con sensor CMOS', category: 'Sensores' },
  { id: 'LEW-TOPS-171', title: 'Motor eléctrico superconductor para aviones', category: 'Aeronáutica' },
  { id: 'ARC-TOPS-45', title: 'Planificador automático de tareas para robots', category: 'Software' },
  { id: 'MSC-TOPS-112', title: 'Monitor portátil de radiación personal', category: 'Salud' },
  { id: 'GSC-TOPS-233', title: 'Sensor de temperatura de fibra óptica para motores', category: 'Sensores' },
  { id: 'LAR-TOPS-276', title: 'Material compuesto ligero para fuselajes de aviones', category: 'Aeronáutica' },
]

export const patentCategories = ['Materiales', 'Sensores', 'Aeronáutica', 'Software', 'Salud'] as const

/** Places to recognise on the globe, as [longitude, latitude]. */
export const cities: { name: string; at: [number, number] }[] = [
  { name: 'Tokio', at: [139.7, 35.7] },
  { name: 'Sídney', at: [151.2, -33.9] },
  { name: 'Pekín', at: [116.4, 39.9] },
  { name: 'Delhi', at: [77.2, 28.6] },
  { name: 'Nairobi', at: [36.8, -1.3] },
  { name: 'Lagos', at: [3.4, 6.5] },
  { name: 'Madrid', at: [-3.7, 40.4] },
  { name: 'São Paulo', at: [-46.6, -23.5] },
  { name: 'Lima', at: [-77, -12] },
  { name: 'Nueva York', at: [-74, 40.7] },
  { name: 'México', at: [-99.1, 19.4] },
  { name: 'Los Ángeles', at: [-118.2, 34] },
  { name: 'Honolulu', at: [-157.9, 21.3] },
]

/** Continents drawn coarsely, as [longitude, latitude] outlines: enough to see which side faces us. */
export const continents: [number, number][][] = [
  // Africa
  [[-17, 21], [-10, 35], [10, 37], [32, 31], [43, 12], [51, 12], [40, -5], [40, -15], [35, -25], [20, -35], [12, -17], [9, 4], [-8, 4], [-17, 14]],
  // South America
  [[-80, 9], [-60, 11], [-35, -5], [-40, -22], [-58, -38], [-68, -55], [-75, -45], [-71, -18], [-81, -5]],
  // North America
  [[-165, 65], [-140, 70], [-95, 72], [-65, 60], [-55, 50], [-70, 43], [-81, 25], [-97, 26], [-87, 15], [-80, 9], [-92, 15], [-105, 20], [-117, 32], [-125, 48], [-135, 58], [-150, 60]],
  // Greenland
  [[-45, 60], [-20, 70], [-20, 82], [-60, 82], [-55, 75], [-50, 68]],
  // Eurasia
  [[-10, 36], [-9, 43], [-2, 48], [5, 53], [10, 57], [5, 62], [15, 69], [30, 70], [60, 70], [80, 73], [110, 77], [140, 72], [170, 68], [178, 65], [160, 58], [140, 52], [135, 35], [122, 30], [120, 22], [108, 20], [105, 10], [100, 13], [98, 8], [92, 22], [80, 15], [77, 8], [72, 20], [67, 24], [57, 25], [52, 15], [43, 12], [35, 30], [28, 37], [26, 41], [15, 40], [3, 43]],
  // Australia
  [[114, -22], [122, -17], [131, -11], [137, -12], [142, -11], [146, -19], [153, -27], [150, -37], [141, -38], [131, -31], [115, -34]],
]
