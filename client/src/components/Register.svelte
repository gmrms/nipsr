<script>

    let identifierRequest = {}

    let calculated = false;
    let available = false;
    let pricing = 0;

    function onSubmit(){
        console.log("onSubmit")
    }

    function getApiHost() {
        return import.meta.env.PUBLIC_NIPSR_API_HOST ? import.meta.env.PUBLIC_NIPSR_API_HOST : '' // use local host if not set
    }

    function fetchAvailability(identifier){
        return fetch(`${getApiHost()}/api/identifier/${identifier}`)
            .then(res => res.json())
    }

    async function calculatePricing(){
        if(!identifierRequest.identifier) {
            calculated = false
            return
        }
        let availability = await fetchAvailability(identifierRequest.identifier)
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
            <select name="domain" bind:value={identifierRequest.domain}>
                <option value="nipsr.io">nipsr.io</option>
                <option value="nipsr.com">nipsr.com</option>
                <option value="nipsr.com.br">nipsr.com.br</option>
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
</div>

<style>
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
</style>