const CACHE_NAME = 'sarkariguru-v3.0.0';
const PRECACHE_ASSETS = [
  '/',
  '/index.html',
  '/manifest.json'
];

// Installation: Cache core assets and skip waiting immediately
self.addEventListener('install', event => {
  console.log('[SarkariGuru SW] Installing new version:', CACHE_NAME);
  event.waitUntil(
    caches.open(CACHE_NAME).then(cache => {
      console.log('[SarkariGuru SW] Precaching assets...');
      return cache.addAll(PRECACHE_ASSETS);
    }).then(() => {
      return self.skipWaiting();
    }).catch(err => {
      console.error('[SarkariGuru SW] Precache failed:', err);
    })
  );
});

// Activation: Clear old caches and claim all open clients
self.addEventListener('activate', event => {
  console.log('[SarkariGuru SW] Activating SW version:', CACHE_NAME);
  event.waitUntil(
    caches.keys().then(cacheNames => {
      return Promise.all(
        cacheNames.map(cacheName => {
          if (cacheName !== CACHE_NAME && cacheName.startsWith('sarkariguru-')) {
            console.log('[SarkariGuru SW] Deleting obsolete cache:', cacheName);
            return caches.delete(cacheName);
          }
        })
      );
    }).then(() => {
      console.log('[SarkariGuru SW] Claiming clients...');
      return self.clients.claim();
    }).then(() => {
      // Notify clients of SW activation
      return self.clients.matchAll().then(clients => {
        clients.forEach(client => {
          client.postMessage({ type: 'SW_UPDATED', version: CACHE_NAME });
        });
      });
    })
  );
});

// Listen for skipWaiting command from client
self.addEventListener('message', event => {
  if (event.data && event.data.action === 'skipWaiting') {
    self.skipWaiting();
  }
});

// Fetching: Network-First strategy with dynamic caching fallback
self.addEventListener('fetch', event => {
  // Ignore non-GET or non-http/https requests
  if (event.request.method !== 'GET' || !event.request.url.startsWith('http')) {
    return;
  }

  // Network first strategy
  event.respondWith(
    fetch(event.request)
      .then(networkResponse => {
        // Cache successful responses for offline access
        if (networkResponse && networkResponse.status === 200 && event.request.url.startsWith(self.location.origin)) {
          const responseToCache = networkResponse.clone();
          caches.open(CACHE_NAME).then(cache => {
            cache.put(event.request, responseToCache);
          });
        }
        return networkResponse;
      })
      .catch(err => {
        console.warn('[SarkariGuru SW] Network request failed, falling back to cache:', event.request.url, err);
        return caches.match(event.request).then(cachedResponse => {
          if (cachedResponse) {
            return cachedResponse;
          }
          // Default fallback for page navigations
          if (event.request.mode === 'navigate') {
            return caches.match('/index.html') || caches.match('/');
          }
          return new Response('Network error and no offline cache available.', {
            status: 503,
            headers: { 'Content-Type': 'text/plain' }
          });
        });
      })
  );
});
