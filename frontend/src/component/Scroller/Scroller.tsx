import * as React from 'react';
import styled from 'styled-components';

const ITEM_HEIGHT = 20;
const ITEM_COUNT = 2500;
const HEIGHT = 500;

const Container = styled.div`
    height: 100%;
    width: 100%;
    background-color: #999;
    overflow: auto;
`;

const Envelope = styled.div<{ minHeight: number}>`
    position: relative;    
    width: 100%;
    ${({ minHeight }) => `min-height: ${minHeight}px;`}    
`;

const ContentContainer = styled.div<{ top: number; elementHeight: number; even: boolean;}>`
    position: absolute;   
    width: 100%; 
    background-color: red;
    border: 5px solid back;
    // height: 400px;        
    // top: 100px;
    ${({ even }) => `background-color: ${even ? 'red' : 'green'};`}    
    ${({ elementHeight }) => `height: ${elementHeight}px;`}    
    ${({ top }) => `top: ${top}px;`}    
`;

const isEven = (value: number) => value % 2 === 0;

const randomInt = (max: number) => Math.floor(Math.random() * max);


const Scroller: React.FC = () => {
    const [even, setEven] = React.useState<number>(0);    
    const [odd, setOdd] = React.useState<number>(1);                 

    const handleScroll = (e: any) => {
        const scrollTop = e.currentTarget.scrollTop;
        const currentIndex = Math.floor(scrollTop / HEIGHT);
        if (isEven(currentIndex)) {
            setEven(currentIndex);
            setOdd(currentIndex + 1);     
        } else {
            setOdd(currentIndex);
            setEven(currentIndex + 1);     
        }
    };

    const evenContainerRef = React.useRef<HTMLInputElement>(null)
    const oddContainerRef = React.useRef<HTMLInputElement>(null)

    React.useLayoutEffect(() => {
        console.log(`EVEN HEIGHT ${evenContainerRef.current?.offsetHeight}`);
        console.log(`ODD HEIGHT ${oddContainerRef.current?.offsetHeight}`);
      }, [even, odd]
    );

    let evenElement = React.useMemo(() => (
        <ContentContainer top={even * HEIGHT} elementHeight={HEIGHT} even={true} ref={evenContainerRef}>
            <div onClick={() => console.log('click')} style={{backgroundColor: 'red', border: "5px solid black"}}>EVEN {even}</div>
        </ContentContainer>
    ), [even])
    let oddElement = React.useMemo(() => (
        <ContentContainer top={odd * HEIGHT} elementHeight={HEIGHT} even={false} ref={oddContainerRef}>
            <div onClick={() => console.log('click')} style={{backgroundColor: 'green', border: "5px solid black"}}>ODD {odd}</div>
        </ContentContainer>
    ), [odd])


    return (
        <Container className="container" onScroll={handleScroll}>
            <Envelope minHeight={ITEM_HEIGHT * ITEM_COUNT}>
                {evenElement}
                {oddElement}
            </Envelope>
        </Container>
    )
}

export default Scroller;