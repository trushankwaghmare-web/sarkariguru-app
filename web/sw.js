const CACHE_NAME = 'sarkariguru-v2';
const PRECACHE_ASSETS = [
  '/',
  '/manifest.json'
];

// Installation: Cache core assets and skip waiting to activate immediately
self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME).then(cache => {
      console.log('[SarkariGuru SW] Precaching assets...');
      return cache.addAll(PRECACHE_ASSETS);
    }).then(() => {
      return self.skipWaiting();
    })
  );
});

// Activation: Clear old caches and claim clients
self.addEventListener('activate', event => {
  const cacheWhitelist = [CACHE_NAME];
  event.waitUntil(
    caches.keys().then(cacheNames => {
      return Promise.all(
        cacheNames.map(cacheName => {
          if (!cacheWhitelist.includes(cacheName)) {
            console.log('[SarkariGuru SW] Deleting old cache:', cacheName);
            return caches.delete(cacheName);
          }
        })
      );
    }).then(() => {
      console.log('[SarkariGuru SW] Claiming clients...');
      return self.clients.claim();
    })
  );
});

// Fetching: Network-First strategy with fallback to Cache for reliability
self.addEventListener('fetch', event => {
  // Skip non-GET requests or requests from third-party domains (like CDNs, extensions)
  if (event.request.method !== 'GET' || !event.request.url.startsWith(self.location.origin)) {
    return;
  }

  event.respondWith(
    fetch(event.request)
      .then(networkResponse => {
        // If we got a valid response, cache it for offline support
        if (networkResponse && networkResponse.status === 200) {
          const responseToCache = networkResponse.clone();
          caches.open(CACHE_NAME).then(cache => {
            cache.put(event.request, responseToCache);
          });
        }
        return networkResponse;
      })
      .catch(() => {
        // If network request fails (e.g. offline), try the cache
        return caches.match(event.request).then(cachedResponse => {
          if (cachedResponse) {
            return cachedResponse;
          }
          // Default offline fallback for page navigations
          if (event.request.mode === 'navigate') {
            return caches.match('/');
          }
        });
      })
  );
});
