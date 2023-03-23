<script>
    import QrCode from "svelte-qrcode"

    let domains = []

    let identifierRequest = {}

    let calculated = false;
    let available = false;
    let pricing = 0;

    let invoice
    let paid = false

    let checkingForPayment

    fetchDomains()

    async function onSubmit(){
        invoice = await submitOrder()
        paid = false
        if(checkingForPayment) clearInterval(checkingForPayment)
        checkingForPayment = setInterval(() => {
            fetch(`${getApiUrl()}/invoice/${invoice.id}`)
                .then(res => res.json())
                .then(res => {
                    if(res) {
                        paid = true
                        clearInterval(checkingForPayment)
                    }
                })
        }, 2000)
    }

    function formatDate(date){
        return date.toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric' })
    }

    function getApiUrl() {
        return import.meta.env.PUBLIC_NIPSR_API_URL ? import.meta.env.PUBLIC_NIPSR_API_URL : '' // use local host if not set
    }

    function fetchAvailability(){
        return fetch(`${getApiUrl()}/identifier/${identifierRequest.domain}/${identifierRequest.identifier}`)
            .then(res => res.json())
    }

    function fetchDomains(){
        fetch(`${getApiUrl()}/identifier/domains`)
            .then(res => res.json())
            .then(res => {
                domains = res
                identifierRequest.domain = domains[0]
            })
    }

    function submitOrder(){
        return fetch(`${getApiUrl()}/identifier`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(identifierRequest),
        }).then(res => res.json())
    }   

    async function calculatePricing(){
        if(!identifierRequest.identifier) {
            calculated = false
            return
        }
        let availability = await fetchAvailability()
        pricing = availability.price
        available = availability.available
        calculated = true
    }

</script>

<div id="registration" class="page">
    <h2>Registration</h2>
    <p>Registering also grants you access to our private relay at <span>wss://private.nipsr.com</span></p>
    <form on:submit|preventDefault={onSubmit}>
        <div>
            <input name="identifier" type="text" placeholder="you" minlength="2" bind:value={identifierRequest.identifier} on:change={calculatePricing}/>
            <span>@</span>
            <select name="domain" bind:value={identifierRequest.domain} on:change={calculatePricing}>
                {#each domains as domain, i}
                    <option value="{domain}">{domain}</option>
                {/each}
            </select>
        </div>
        <input name="pubkey" type="text" placeholder="pubkey" minlength="32" bind:value={identifierRequest.pubkey}/>
        <p id="availability" class:hasPricing="{calculated}">
            {#if available}
                Available for <span>{pricing}</span> sats
            {:else}
                Unavailable
            {/if}
        </p>
        <button type="submit">SUBMIT</button>
    </form>
    {#if invoice}
        {#if paid}
            <div id="welcome">
                <h2>Welcome <span>{invoice.identifier}</span>!</h2>
                <p>Your NIP-05 identifier <span>{invoice.identifier}@{invoice.domain}</span> and private relay access are already available!</p>
            </div>
        {:else}
            <div id="invoice">
                <h3>Waiting for Payment</h3>
                <div class="lds-dual-ring"></div>
                <p>Pay the invoice to register <span>{invoice.identifier}@{invoice.domain}</span></p>
                <div id="qrcode">
                    <img alt="qrcode" src="favicon.svg"/>
                    <QrCode value="{invoice.data}" errorCorrection="L" color="#FFFFFF" background="#000814" size="1200" />
                </div>
                <p>Amount: <span>{invoice.amount}</span> sats</p>
                <p>Expires at: <span>{formatDate(new Date(invoice.expiration * 1000))}</span></p>
            </div>
        {/if}
    {/if}
</div>

<style>
    #welcome {
        margin-top: 3rem;
    }

    p {
        margin: var(--spacing) 0;
        text-align: center;
    }

    form {
        max-width: 400px;
        margin: 0 auto;
        display: flex;
        flex-direction: column;
        gap: 0.5rem;
    }

    form > div {
        flex-direction: row;
        display: flex;
        align-items: center;
    }

    form > div > span {
        font-size: 1.3rem;
        margin: 0 0.5rem;
    }

    input[type="text"], select {
        color: var(--white);
        border: 1px solid rgba(255, 255, 255, 0.05);
        background-color: rgba(0, 0, 0, 0.3);
        border-radius: 5px;
        font-size: 1.2rem;
        padding: 0.5rem 1rem;
        width: 100%;
        text-align: center;
    }

    select option {
        background-color: var(--dark);
    }

    input[name="identifier"] {
        text-align: right;
    }

    button[type="submit"] {
        cursor: pointer;
        border: none;
        color: var(--accent);
        border: 1px solid rgba(255, 255, 255, 0.05);
        background: linear-gradient(120deg, rgba(255,255,255,0.1) 20%, rgba(255,255,255,0.2) 60%, rgba(255,255,255,0.07) 80%);
        background-size: 150%;
        border-radius: 5px;
        font-size: 1.2rem;
        padding: 0.5rem 1rem;
        width: 100%;
        text-align: center;
        box-shadow: 0 5px 20px 2px #FFC3002d;
    }

    button[type="submit"]:hover {
        border: 1px solid rgba(255, 255, 255, 0.15);
        box-shadow: 0 5px 35px 2px #ffc4005b;
    }

    #availability {
        opacity: 0;
        margin: 0;
        height: 0;
        text-align: center;
        font-size: 1rem;
        overflow: hidden;
    }

    #availability.hasPricing {
        opacity: 1;
        margin: 1rem 0;
        height: 30px;
    }

    #invoice {
        border-radius: 10px;
        margin-top: 3rem;
        padding: 1rem;
        border: 1px solid rgba(255, 255, 255, 0.2);
        background-color: rgba(0, 0, 0, 0.3);
        box-shadow: 0 5px 20px 2px #FFC3002d;
        display: block;
        max-width: 400px;
    }

    #invoice h3 {
        margin: 0;
    }

    #invoice p {
        margin: 1rem 0;
        font-size: 0.8rem;
    }

    #qrcode {
        position: relative;
    }

    #qrcode img {
        position: absolute;
        width: 25%;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background-color: white;
        padding: 5px;
        border-radius: 100%;
    }
    .lds-dual-ring {
        display: block;
        width: 20px;
        height: 20px;
        margin: 0 auto;
    }
    .lds-dual-ring:after {
        content: " ";
        display: block;
        width: 20px;
        height: 20px;
        margin: 1rem 0;
        border-radius: 50%;
        border: 2px solid #fff;
        border-color: #fff transparent #fff transparent;
        animation: lds-dual-ring 2s linear infinite;
    }
    @keyframes lds-dual-ring {
        0% {
            transform: rotate(0deg);
        }
        100% {
            transform: rotate(360deg);
        }
    }

</style>