import * as React from 'react';
import styled from 'styled-components';
import { useReadReport } from '../../api/api';
import { ReportInfo } from '../../model';
import LogComponent from '../LogComponent';
import ContentContainer from './ContentContainer';

const ITEM_HEIGHT = 28;
const ITEM_COUNT = 2500;
const HEIGHT = 560;

interface IScrollerProps {
    report: ReportInfo;
}

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


const isEven = (value: number) => value % 2 === 0;

const randomInt = (max: number) => Math.floor(Math.random() * max);


const Scroller: React.FC<IScrollerProps> = ({
    report: { id, rootSpanCount }
}) => {
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
        <ContentContainer id={id} page={even} pageSize={20} />
    ), [even]);
    let oddElement = React.useMemo(() => (
        <ContentContainer id={id} page={odd} pageSize={20} />
    ), [odd]);

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