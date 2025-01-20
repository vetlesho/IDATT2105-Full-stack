document.querySelectorAll(".block").forEach(block => {
    const button = block.querySelector(".seenbutton");
    const counter = block.querySelector("p");
    let count = 0;

    button.addEventListener("click", () => {
        count++;
        counter.textContent = `Du har sett denne filmen ${count} ganger.`;
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const toggleButton = document.querySelector(".menu .menuelement:nth-child(2) a");
    const movieBlocks = document.querySelectorAll(".content .block");
    let isHidden = false;

    toggleButton.addEventListener("click", (event) => {
        isHidden = !isHidden;
        movieBlocks.forEach(block => {
            block.style.display = isHidden ? "none" : "grid";
        });

        toggleButton.textContent = isHidden ? "Vis filmer" : "Skjul filmer";
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const genres = ["Action", "Drama", "Comedy", "Thriller", "Sci-Fi"];
    const blocks = document.querySelectorAll(".block");

    blocks.forEach(block => {
        const genreParagraph = block.querySelector(".moviegenre");
        const randomGenre = genres[Math.floor(Math.random() * genres.length)];

        genreParagraph.textContent = `Filmens sjanger: ${randomGenre}`;
    });
});