document.getElementById('convert').addEventListener('click', () => {
  const amountEl = document.getElementById('amount');
  const fromEl   = document.getElementById('from');
  const toEl     = document.getElementById('to');
  const resultEl = document.getElementById('result');

  const amount = encodeURIComponent(amountEl.value);
  const from   = encodeURIComponent(fromEl.value.trim().toUpperCase());
  const to     = encodeURIComponent(toEl.value.trim().toUpperCase());

  resultEl.textContent = 'Loading…';

  fetch(`/convert?amount=${amount}&from=${from}&to=${to}`)
    .then(resp => {
      if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
      return resp.json();
    })
    .then(data => {
      const { amount, from, to, rate, result } = data;
      resultEl.textContent =
        `${amount} ${from} = ${result.toFixed(2)} ${to} (Rate: ${rate.toFixed(4)})`;
    })
    .catch(err => {
      resultEl.textContent = `Error: ${err.message}`;
    });
});
