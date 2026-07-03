import pathlib
import zlib
import struct

p = pathlib.Path('src/main/resources/assets/wingbound/textures/entity/baby_fire_dragon.png')
p.parent.mkdir(parents=True, exist_ok=True)

w, h = 16, 16
pixels = []
for y in range(h):
    for x in range(w):
        if x == y or x == w - 1 - x:
            pixels.extend((255, 0, 0, 255))
        else:
            pixels.extend((255, 192, 0, 255))

raw = b''.join(b'\x00' + bytes(pixels[y * w * 4:(y + 1) * w * 4]) for y in range(h))

def chunk(type_bytes, data_bytes):
    return struct.pack('>I', len(data_bytes)) + type_bytes + data_bytes + struct.pack('>I', zlib.crc32(type_bytes + data_bytes) & 0xffffffff)

png = b'\x89PNG\r\n\x1a\n'
png += chunk(b'IHDR', struct.pack('>IIBBBBB', w, h, 8, 6, 0, 0, 0))
png += chunk(b'IDAT', zlib.compress(raw))
png += chunk(b'IEND', b'')

p.write_bytes(png)
print('created', p)
