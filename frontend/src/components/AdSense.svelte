<script>
  import { onMount } from 'svelte';

  export let client = 'ca-pub-XXXXXXXXXXXXXXXXX'; // AdSense 클라이언트 ID
  export let slot = 'XXXXXXXXXX'; // 광고 슬롯 ID
  export let format = 'auto'; // 광고 형식 (auto, rectangle, horizontal, vertical)
  export let responsive = true; // 반응형 여부
  export let style = 'display:block'; // 추가 스타일

  let adContainer;
  let adPushed = false;

  onMount(() => {
    // 스크립트가 이미 로드되어 있는지 확인
    if (!window.adsbygoogle) {
      const script = document.createElement('script');
      script.src = `https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=${client}`;
      script.async = true;
      script.crossOrigin = 'anonymous';
      document.head.appendChild(script);

      script.onload = () => {
        pushAd();
      };
    } else {
      pushAd();
    }

    return () => {
      adPushed = false;
    };
  });

  function pushAd() {
    if (!adPushed && adContainer) {
      try {
        (window.adsbygoogle = window.adsbygoogle || []).push({});
        adPushed = true;
      } catch (e) {
        console.error('AdSense error:', e);
      }
    }
  }
</script>

<div class="adsense-wrapper">
  <div class="ad-label">Advertisement</div>
  <ins
    bind:this={adContainer}
    class="adsbygoogle"
    style={style}
    data-ad-client={client}
    data-ad-slot={slot}
    data-ad-format={format}
    data-full-width-responsive={responsive.toString()}
  ></ins>
</div>

<style>
  .adsense-wrapper {
    margin: 24px 0;
    padding: 16px;
    background: var(--bg-card);
    border: 2px solid var(--border-color);
    border-radius: 12px;
    text-align: center;
  }

  .ad-label {
    font-family: var(--font-display);
    font-size: 0.7rem;
    color: var(--text-secondary);
    text-transform: uppercase;
    letter-spacing: 1px;
    margin-bottom: 8px;
    opacity: 0.6;
  }

  .adsbygoogle {
    min-height: 100px;
  }

  @media (max-width: 768px) {
    .adsense-wrapper {
      margin: 16px 0;
      padding: 12px;
    }
  }
</style>
