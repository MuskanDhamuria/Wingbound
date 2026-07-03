const fs = require('fs');
const path = require('path');
const zlib = require('zlib');

const dir = path.join('src', 'main', 'resources', 'assets', 'wingbound', 'textures', 'entity');
fs.mkdirSync(dir, { recursive: true });
const file = path.join(dir, 'baby_fire_dragon.png');
const w = 16;
const h = 16;
const pixels = [];
for (let y = 0; y < h; y++) {
  for (let x = 0; x < w; x++) {
    if (x === y || x === w - 1 - x) {
      pixels.push(255, 0, 0, 255);
    } else {
      pixels.push(255, 192, 0, 255);
    }
  }
}
const raw = Buffer.concat(
  Array.from({ length: h }, (_, y) => Buffer.concat([Buffer.from([0]), Buffer.from(pixels.slice(y * w * 4, (y + 1) * w * 4))]))
);

function crc32(buf) {
  let crc = 0xffffffff;
  for (let i = 0; i < buf.length; i++) {
    crc ^= buf[i];
    for (let j = 0; j < 8; j++) {
      crc = (crc >>> 1) ^ (0xedb88320 & -(crc & 1));
    }
  }
  return (crc ^ 0xffffffff) >>> 0;
}

const chunk = (type, data) => {
  const len = Buffer.alloc(4);
  len.writeUInt32BE(data.length, 0);
  const typeBuf = Buffer.from(type);
  const crc = Buffer.alloc(4);
  crc.writeUInt32BE(crc32(Buffer.concat([typeBuf, data])), 0);
  return Buffer.concat([len, typeBuf, data, crc]);
};
const png = Buffer.concat([
  Buffer.from([0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a]),
  chunk('IHDR', Buffer.from([0, 0, 0, w, 0, 0, 0, h, 8, 6, 0, 0, 0])),
  chunk('IDAT', zlib.deflateSync(raw)),
  chunk('IEND', Buffer.alloc(0)),
]);
fs.writeFileSync(file, png);
console.log('created', file);
