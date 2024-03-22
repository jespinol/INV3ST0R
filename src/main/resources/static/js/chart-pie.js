// HTML and scripts modified from Start Bootstrap - SB Admin 2 (https://startbootstrap.com/theme/sb-admin-2/)
// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

const ctxPieChart = document.getElementById("pieChart");

const pieChart = new Chart(ctxPieChart, {
    type: 'doughnut',
    data: {
        labels: pieChartLabels,
        datasets: [{
            data: pieChartData,
            backgroundColor: pieChartBackgroundColors,
            hoverBackgroundColor: pieChartHoverBackGroundColors,
            hoverBorderColor: "rgba(234, 236, 244, 1)",
        }],
    },
    options: {
        maintainAspectRatio: false,
        tooltips: {
            backgroundColor: "rgb(255,255,255)",
            bodyFontColor: "#858796",
            borderColor: '#dddfeb',
            borderWidth: 1,
            xPadding: 15,
            yPadding: 15,
            displayColors: true,
            caretPadding: 10,
        },
        legend: {
            display: false
        },
        cutoutPercentage: 70,
    },
});