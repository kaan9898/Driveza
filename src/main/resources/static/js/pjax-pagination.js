// Simple PJAX for pagination: swaps only the results container instead of reloading the whole page.
// Usage: wrap the area to update with `data-pjax-container` + a stable `id`, and mark links with `data-pjax`.
(function () {
  async function loadInto(container, url, pushState) {
    const response = await fetch(url, {
      credentials: "same-origin",
      headers: { "X-PJAX": "true" }
    });
    const html = await response.text();

    const doc = new DOMParser().parseFromString(html, "text/html");
    const nextContainer = doc.getElementById(container.id);
    if (!nextContainer) {
      // Fallback if the expected container isn't present (e.g., template changed).
      if (pushState) window.location.assign(url);
      return;
    }

    container.replaceWith(nextContainer);
    if (pushState) history.pushState({}, "", url);
  }

  document.addEventListener("click", (event) => {
    const link = event.target.closest("a[data-pjax]");
    if (!link) return;

    // Keep normal browser behaviors.
    if (event.defaultPrevented) return;
    if (event.metaKey || event.ctrlKey || event.shiftKey || event.altKey) return;
    if (link.target && link.target !== "_self") return;

    const container = link.closest("[data-pjax-container]");
    if (!container || !container.id) return;

    const href = link.getAttribute("href");
    if (!href) return;

    event.preventDefault();
    loadInto(container, href, true).catch(() => window.location.assign(href));
  });

  window.addEventListener("popstate", () => {
    const container = document.querySelector("[data-pjax-container]");
    if (!container || !container.id) return;
    loadInto(container, window.location.href, false).catch(() => window.location.reload());
  });
})();

