
document.addEventListener("DOMContentLoaded", () =>
{
    loadTooltips();
});

function loadTooltips()
{
    const tooltips = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltips.forEach(tooltip => new bootstrap.Tooltip(tooltip));
}